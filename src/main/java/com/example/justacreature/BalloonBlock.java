package com.example.justacreature;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);

        // Only drop the block itself if the tool has Silk Touch; otherwise pop nothing
        if (hasSilkTouch(tool)) {
            return super.getDrops(state, builder);
        }

        return Collections.emptyList();
    }

    /**
     * Returns true if the given ItemStack has Silk Touch.
     * Uses NeoForge 1.21.1 registry-aware EnchantmentHelper with tag lookup.
     */
    private static boolean hasSilkTouch(ItemStack tool) {
        if (tool == null || tool.isEmpty()) return false;
        return EnchantmentHelper.getTagEnchantmentLevel(EnchantmentTags.SILK_TOUCH, tool) > 0;
    }
}
