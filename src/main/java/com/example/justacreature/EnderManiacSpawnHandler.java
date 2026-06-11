package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = JustaCreature.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EnderManiacSpawnHandler {

    private static final int SPAWN_CHANCE = 10;

    private static Block[] BALLOON_BLOCKS = null;

    private static Block[] getBalloonBlocks() {
        if (BALLOON_BLOCKS == null) {
            BALLOON_BLOCKS = new Block[] {
                    JustaCreature.WHITE_BALLOON.get(),
                    JustaCreature.RED_BALLOON.get(),
                    JustaCreature.BLUE_BALLOON.get(),
                    JustaCreature.PINK_BALLOON.get(),
                    JustaCreature.GREEN_BALLOON.get(),
                    JustaCreature.YELLOW_BALLOON.get(),
                    JustaCreature.ORANGE_BALLOON.get(),
                    JustaCreature.PURPLE_BALLOON.get(),
                    JustaCreature.BLACK_BALLOON.get(),
                    JustaCreature.GRAY_BALLOON.get(),
                    JustaCreature.BROWN_BALLOON.get(),
                    JustaCreature.LIME_BALLOON.get(),
                    JustaCreature.CYAN_BALLOON.get(),
                    JustaCreature.MAGENTA_BALLOON.get(),
                    JustaCreature.LIGHT_BLUE_BALLOON.get(),
                    JustaCreature.LIGHT_GRAY_BALLOON.get()
            };
        }
        return BALLOON_BLOCKS;
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel))
            return;
        if (serverLevel.dimension() != Level.END)
            return;
        if (!event.isNewChunk())
            return;
        if (!(event.getChunk() instanceof LevelChunk chunk))
            return;

        if (serverLevel.getRandom().nextInt(100) >= SPAWN_CHANCE)
            return;

        // Collect all chorus plant / flower positions in this chunk
        List<BlockPos> chorusPositions = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int chunkX = chunk.getPos().getMinBlockX();
        int chunkZ = chunk.getPos().getMinBlockZ();

        for (int x = chunkX; x < chunkX + 16; x++) {
            for (int z = chunkZ; z < chunkZ + 16; z++) {
                for (int y = serverLevel.getMinBuildHeight(); y < serverLevel.getMaxBuildHeight(); y++) {
                    mutable.set(x, y, z);
                    Block block = serverLevel.getBlockState(mutable).getBlock();
                    if (block == Blocks.CHORUS_PLANT) {
                        chorusPositions.add(mutable.immutable());
                    }
                }
            }
        }

        if (chorusPositions.isEmpty())
            return;

        // Pick a random chorus block anywhere in the structure
        BlockPos anyChorusPos = chorusPositions.get(serverLevel.getRandom().nextInt(chorusPositions.size()));

        // Walk DOWN the same X/Z column to find the LOWEST chorus block in this stem
        BlockPos balloonPos = findBlockBelowLowestChorus(serverLevel, anyChorusPos);
        if (balloonPos == null)
            return;
        if (!serverLevel.getBlockState(balloonPos).isAir())
            return;

        // Place a random balloon directly under the base of the chorus plant
        Block[] balloons = getBalloonBlocks();
        Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
        serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), Block.UPDATE_ALL);

        // Find ground nearby and spawn exactly ONE EnderManiac
        BlockPos spawnPos = findGroundNear(serverLevel, balloonPos);
        if (spawnPos == null)
            return;

        EnderManiac mob = JustaCreatureMobs.ENDER_MANIAC.get().create(serverLevel);
        if (mob == null)
            return;
        mob.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                serverLevel.getRandom().nextFloat() * 360f, 0f);
        mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                net.minecraft.world.entity.MobSpawnType.CHUNK_GENERATION, null);
        serverLevel.addFreshEntity(mob);
    }

    /**
     * Starting from any block in a chorus structure, walks down the same X/Z column
     * to find the LOWEST chorus block. Returns the air block directly below that —
     * i.e., the spot right under the base of the plant.
     * Returns null if no valid air block is found.
     */
    private static BlockPos findBlockBelowLowestChorus(ServerLevel level, BlockPos anyChorusPos) {
        BlockPos.MutableBlockPos current = anyChorusPos.mutable();

        // Walk down until the block below is no longer a chorus block
        while (true) {
            Block below = level.getBlockState(current.below()).getBlock();
            if (below == Blocks.CHORUS_PLANT || below == Blocks.CHORUS_FLOWER) {
                current.move(0, -1, 0);
            } else {
                break;
            }
        }

        // current is now the lowest chorus block; the block below it is the target
        BlockPos target = current.below().immutable();
        return level.getBlockState(target).isAir() ? target : null;
    }

    /**
     * Finds a solid ground position within ±4 blocks of the balloon.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int offsetX = level.getRandom().nextIntBetweenInclusive(-16, 16);
        int offsetZ = level.getRandom().nextIntBetweenInclusive(-16, 16);
        BlockPos searchStart = balloonPos.offset(offsetX, 0, offsetZ);

        for (int dy = 0; dy > -30; dy--) {
            BlockPos candidate = searchStart.offset(0, dy, 0);
            if (level.getBlockState(candidate).isAir() && !level.getBlockState(candidate.below()).isAir()) {
                return candidate;
            }
        }
        return null;
    }
}
