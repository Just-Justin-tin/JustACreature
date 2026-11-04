package com.example.justacreature;

import com.mojang.serialization.MapCodec;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CorarchelonEgg extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<CorarchelonEgg> CODEC = simpleCodec(CorarchelonEgg::new);

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 3);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape ONE_EGG_SHAPE = Block.box(3.0D, 0.0D, 1.0D, 13.0D, 7.0D, 8.0D);
    private static final VoxelShape MULTI_EGG_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);

    public CorarchelonEgg(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HATCH, Integer.valueOf(0))
                .setValue(EGGS, Integer.valueOf(1))
                .setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected MapCodec<CorarchelonEgg> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, EGGS, WATERLOGGED);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(EGGS) < 3
                ? true
                : super.canBeReplaced(state, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState currentState = context.getLevel().getBlockState(context.getClickedPos());
        if (currentState.is(this)) {
            return currentState.setValue(EGGS, Math.min(3, currentState.getValue(EGGS) + 1));
        }

        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean isWater = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(isWater));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(EGGS) == 1 ? ONE_EGG_SHAPE : MULTI_EGG_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
            BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            return state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        }
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.CORAL_BLOCKS);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            if (this.canHatch(level)) {
                int i = state.getValue(HATCH);
                if (i < 2) {
                    level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F,
                            0.9F + random.nextFloat() * 0.2F);
                    level.setBlock(pos, state.setValue(HATCH, Integer.valueOf(i + 1)), 2);
                } else {
                    level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F,
                            0.9F + random.nextFloat() * 0.2F);
                    level.destroyBlock(pos, false);

                    int eggCount = state.getValue(EGGS);
                    for (int j = 0; j < eggCount; ++j) {
                        level.levelEvent(2001, pos, Block.getId(state));
                        Corarchelon corarchelon = JustaCreatureMobs.CORARCHELON.get().create(level);
                        if (corarchelon != null) {
                            corarchelon.setAge(-24000); // Make it a baby
                            corarchelon.moveTo((double) pos.getX() + 0.3D + (double) j * 0.2D, (double) pos.getY(),
                                    (double) pos.getZ() + 0.3D, 0.0F, 0.0F);
                            level.addFreshEntity(corarchelon);
                        }
                    }
                }
            }
        }
    }

    private boolean canHatch(Level level) {
        return level.random.nextInt(5) == 0; // Much faster hatch for testing, adjust as needed. Vanilla is usually ~500
    }
}
