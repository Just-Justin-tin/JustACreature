package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CrystallineMelon extends HorizontalDirectionalBlock {
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

    public static final MapCodec<CrystallineMelon> CODEC = simpleCodec(CrystallineMelon::new);

    @Override
    protected MapCodec<CrystallineMelon> codec() {
        return CODEC;
    }

    public CrystallineMelon(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, BedPart.FOOT)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    // --- THE NEW VANILLA-STYLE DESTRUCTION LOGIC ---
    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
            BlockPos currentPos, BlockPos facingPos) {
        // If the block updating next to us is supposed to be our other half...
        if (facing == getConnectedDirection(state)) {
            // If the other half is still our melon, stay alive. If it's missing, break
            // instantly!
            return facingState.is(this) && facingState.getValue(PART) != state.getValue(PART) ? state
                    : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    // Helper method to easily find which direction the other half is located
    private Direction getConnectedDirection(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos headPos = pos.relative(direction);
        Level level = context.getLevel();

        // Check if the block in front is replaceable so we have room for the second
        // half
        if (level.getBlockState(headPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(headPos)) {
            return this.defaultBlockState().setValue(FACING, direction).setValue(PART, BedPart.FOOT);
        } else {
            return null; // Cancel placement if there is a wall in the way
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos headPos = pos.relative(state.getValue(FACING));
            // Place the head part
            level.setBlock(headPos, state.setValue(PART, BedPart.HEAD), 3);
            // Force an update so the blocks connect properly
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }
}