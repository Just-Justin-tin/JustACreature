package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.registries.DeferredBlock;

public class CrystallineStem extends StemBlock {
    private final DeferredBlock<Block> myFruit;
    private final DeferredBlock<Block> myAttachedStem;

    // Notice the new ResourceKey<net.minecraft.world.item.Item> seedKey added here!
    public CrystallineStem(ResourceKey<Block> fruitKey, ResourceKey<Block> attachedKey,
            ResourceKey<net.minecraft.world.item.Item> seedKey,
            DeferredBlock<Block> fruit, DeferredBlock<Block> attachedStem,
            net.minecraft.world.level.block.state.BlockBehaviour.Properties props) {
        super(fruitKey, attachedKey, seedKey, props);
        this.myFruit = fruit;
        this.myAttachedStem = attachedStem;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            // A 1-in-5 chance to grow perfectly mimics normal crop speed
            if (random.nextInt(5) == 0) {
                int age = state.getValue(AGE);
                if (age < 7) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 2);
                } else {
                    // --- UPGRADED 1x2 GROWTH LOGIC ---
                    // Create an array of all 4 directions
                    Direction[] dirs = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH,
                            Direction.WEST };

                    // Pick a random starting point in the array so it doesn't always favor one side
                    int startIdx = random.nextInt(4);

                    // Loop through all 4 directions instantly
                    for (int i = 0; i < 4; i++) {
                        Direction dir = dirs[(startIdx + i) % 4];
                        BlockPos footPos = pos.relative(dir);
                        BlockPos headPos = footPos.relative(dir); // The block right behind the foot

                        // Check if BOTH blocks are empty, and BOTH have solid dirt/farmland underneath
                        if (level.isEmptyBlock(footPos) && level.getBlockState(footPos.below()).is(BlockTags.DIRT) &&
                                level.isEmptyBlock(headPos)
                                && level.getBlockState(headPos.below()).is(BlockTags.DIRT)) {

                            // 1. Place the Foot Half
                            level.setBlock(footPos, myFruit.get().defaultBlockState()
                                    .setValue(CrystallineMelon.FACING, dir)
                                    .setValue(CrystallineMelon.PART, BedPart.FOOT), 3);
                            // 2. Place the Head Half
                            level.setBlock(headPos, myFruit.get().defaultBlockState()
                                    .setValue(CrystallineMelon.FACING, dir)
                                    .setValue(CrystallineMelon.PART, BedPart.HEAD), 3);
                            // 3. Turn the stem into the Attached Stem
                            level.setBlock(pos, myAttachedStem.get().defaultBlockState()
                                    .setValue(HorizontalDirectionalBlock.FACING, dir), 3);

                            // Stop looking immediately once we successfully plant it!
                            break;
                        }
                    }
                }
            }
        }
    }
}