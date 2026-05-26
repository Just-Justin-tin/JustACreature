package com.example.justacreature;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        // Makes it so entities can move through it
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        // If a player steps on it, they float up gently
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity) {
            // Small upward velocity to simulate floating
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0, 0.15, 0));

            // Apply a small horizontal push sometimes to make it fun
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

    // FIXED: Updated signature to LootParams.Builder
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        Player player = builder.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Player p ? p : null;
        ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);

        boolean isHand = tool == null || tool.isEmpty();

        // Note: Manual Silk Touch checking is removed here because 1.21 requires
        // registry
        // lookups for enchantments. You should manage Silk Touch drops via a Block Loot
        // Table JSON instead.
        if (isHand) {
            return super.getDrops(state, builder);
        }
        return Collections.emptyList();
    }
}