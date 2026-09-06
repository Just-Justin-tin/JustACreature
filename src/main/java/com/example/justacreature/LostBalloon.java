package com.example.justacreature;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.Blocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

public class LostBalloon extends Block {

    public static final BooleanProperty NATURALLY_GENERATED = BooleanProperty.create("naturally_generated");

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public LostBalloon() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_MAGENTA)
                .noCollission() // remove if you want players to bump into it
                .strength(4.0F)
                .sound(net.minecraft.world.level.block.SoundType.WOOL)
                .noOcclusion()); // required for any non-full-cube model

        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
        this.registerDefaultState(this.stateDefinition.any().setValue(NATURALLY_GENERATED, false));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state,
            net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            ItemStack tool = player.getMainHandItem();
            var enchantments = tool.get(DataComponents.ENCHANTMENTS);
            boolean hasSilkTouch = enchantments != null && enchantments.keySet().stream()
                    .anyMatch(h -> h.is(EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING));

            if (hasSilkTouch) {
                Block.popResource(level, pos, new ItemStack(this.asItem()));
                if (state.getValue(NATURALLY_GENERATED)) {
                    net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(20.0D);
                    java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                    for (EnderManiac maniac : nearbyManiacs) {
                        maniac.setForcedPersistent(false);
                        maniac.setTrading(true);
                        maniac.setCanLookAtPlayer(true);
                    }
                }

            } else {
                if (state.getValue(NATURALLY_GENERATED)) {
                    // Search in a 10-block radius around the balloon
                    net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(20.0D);
                    java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                    for (EnderManiac maniac : nearbyManiacs) {
                        if (!maniac.isTrading() && !state.getValue(NATURALLY_GENERATED)) {
                            maniac.setForcedPersistent(false);
                            maniac.setRevealed(true); // Triggers the synced data change
                            maniac.setDisguised(false); // Optional: turn off the disguise state
                        }
                    }
                }
            }
        }

        player.playSound(SoundEvents.LAVA_POP, 1.0F, 15.0F);
        return super.playerWillDestroy(level, pos, state, player);

    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null; // can't place if there's no room above
    }

    @Override
    protected void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock())) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this) && otherState.getValue(HALF) != half) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NATURALLY_GENERATED);
    }
}