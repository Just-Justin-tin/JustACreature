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

@EventBusSubscriber(modid = JustaCreature.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EnderManiacSpawnHandler {

    private static final int SPAWN_CHANCE = 10;
    // How many random columns to probe before giving up
    private static final int MAX_PROBES = 12;

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
        try {
            if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
            if (serverLevel.dimension() != Level.END) return;
            if (!event.isNewChunk()) return;
            if (!(event.getChunk() instanceof LevelChunk chunk)) return;

            if (serverLevel.getRandom().nextInt(100) >= SPAWN_CHANCE) return;

            int chunkX = chunk.getPos().getMinBlockX();
            int chunkZ = chunk.getPos().getMinBlockZ();
            int minY = serverLevel.getMinBuildHeight();
            int maxY = serverLevel.getMaxBuildHeight();

            // Probe up to MAX_PROBES random columns — cheap, no full scan
            BlockPos balloonPos = null;
            for (int probe = 0; probe < MAX_PROBES; probe++) {
                int rx = chunkX + serverLevel.getRandom().nextInt(16);
                int rz = chunkZ + serverLevel.getRandom().nextInt(16);

                // Scan this one column bottom-up for a chorus block with air below it
                for (int y = minY + 1; y < maxY; y++) {
                    BlockPos pos = new BlockPos(rx, y, rz);
                    Block b = serverLevel.getBlockState(pos).getBlock();
                    if ((b == Blocks.CHORUS_PLANT || b == Blocks.CHORUS_FLOWER)
                            && serverLevel.getBlockState(pos.below()).isAir()) {
                        balloonPos = pos.below();
                        break;
                    }
                }
                if (balloonPos != null) break;
            }

            // No valid chorus found in any probe — bail
            if (balloonPos == null) return;

            // Place balloon
            Block[] balloons = getBalloonBlocks();
            Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
            serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), Block.UPDATE_ALL);

            // Find ground to spawn on — search in the same column first, then nearby
            BlockPos spawnPos = findGroundNear(serverLevel, balloonPos);
            if (spawnPos == null) return;

            EnderManiac mob = JustaCreatureMobs.ENDER_MANIAC.get().create(serverLevel);
            if (mob == null) return;
            mob.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                    serverLevel.getRandom().nextFloat() * 360f, 0f);
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                    net.minecraft.world.entity.MobSpawnType.CHUNK_GENERATION, null);
            serverLevel.addFreshEntity(mob);

        } catch (Exception e) {
            JustaCreature.LOGGER.error("[JustACreature] EnderManiacSpawnHandler failed: {}", e.getMessage());
        }
    }

    /**
     * Searches for solid ground near the balloon.
     * Tries the balloon's own column first, then several nearby offsets.
     * The End is mostly void, so we need multiple attempts.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int minY = level.getMinBuildHeight();

        // Try the balloon column and 8 nearby offsets
        int[][] offsets = {
            {0, 0}, {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {2, 1}, {-2, 1}, {1, -2}, {-1, 2}
        };

        for (int[] off : offsets) {
            BlockPos.MutableBlockPos candidate = balloonPos
                .offset(off[0], 0, off[1]).mutable();

            while (candidate.getY() > minY + 1) {
                if (level.getBlockState(candidate).isAir()
                        && !level.getBlockState(candidate.below()).isAir()) {
                    return candidate.immutable();
                }
                candidate.move(0, -1, 0);
            }
        }
        return null;
    }
}
