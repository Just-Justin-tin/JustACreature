package com.example.justacreature;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.component.DataComponents;

public class BalloonBlock extends Block {
    private final DyeColor color;

    public BalloonBlock(DyeColor color) {
        super(Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WHITE_WOOL)
                .strength(0.1F)
                .sound(net.minecraft.world.level.block.SoundType.WOOL)
                .noOcclusion()
                .noCollission());
        this.color = color;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
            CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity) {
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0, 0.15, 0));

            if (pLevel.getRandom().nextBoolean()) {
                pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0.05, 0, 0));
            }
        }
        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state,
            net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide) {
            ItemStack tool = player.getMainHandItem();
            var enchantments = tool.get(DataComponents.ENCHANTMENTS);
            boolean hasSilkTouch = enchantments != null && enchantments.keySet().stream()
                    .anyMatch(h -> h.is(EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING));

            if (hasSilkTouch) {
                Block.popResource(level, pos, new ItemStack(this.asItem()));
                net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(10.0D);
                java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                for (EnderManiac maniac : nearbyManiacs) {
                    maniac.setCanLookAtPlayer(true);
                }
            } else {
                // Search in a 10-block radius around the balloon
                net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(10.0D);
                java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                for (EnderManiac maniac : nearbyManiacs) {
                    maniac.setRevealed(true); // Triggers the synced data change
                    maniac.setDisguised(false); // Optional: turn off the disguise state

                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

}
