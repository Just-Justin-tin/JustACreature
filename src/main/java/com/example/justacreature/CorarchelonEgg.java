package com.example.justacreature;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class CorarchelonEgg extends Block {
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CorarchelonEgg(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HATCH, Integer.valueOf(0))
                .setValue(EGGS, Integer.valueOf(1)));
    }

    public static final MapCodec<CorarchelonEgg> CODEC = simpleCodec(CorarchelonEgg::new);

    @Override
    protected MapCodec<CorarchelonEgg> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // 3. Register the property
        builder.add(HATCH, WATERLOGGED, EGGS, FACING);
    }

    // 4. Check if we are placing the egg into lava
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean isWater = fluidstate.getType() == Fluids.WATER || fluidstate.getType() == Fluids.FLOWING_WATER;

        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(isWater));
    }

    // 5. Tell the game to render/act like lava if the block is lavalogged
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // 6. Ensure lava flows properly out of the block if it updates
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
            BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
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

                    Corarchelon corarchelon = JustaCreatureMobs.CORARCHELON.get().create(level);
                    if (corarchelon != null) {
                        corarchelon.setAge(-24000); // Make it a baby
                        corarchelon.moveTo((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
                                0.0F,
                                0.0F);
                        level.addFreshEntity(corarchelon);
                    }
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return level.getBlockState(pos.relative(facing)).is(BlockTags.CORAL_BLOCKS);
    }

    private boolean canHatch(Level level) {
        return level.random.nextInt(5) == 0; // Much faster hatch for testing, adjust as needed. Vanilla is usually ~500
    }
}
