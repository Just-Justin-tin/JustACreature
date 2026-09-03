package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.entity.EntityType;

@EventBusSubscriber(modid = JustaCreature.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EnderManiacSpawnHandler {

    private static final int SPAWN_CHANCE = 50;
    private static final int MAX_PROBES = 12;

    // Place the balloon with zero side effects on neighbors:
    // UPDATE_CLIENTS (2) - syncs to clients so it renders
    // UPDATE_KNOWN_SHAPE (16) - skips neighbor shape updates (stops chorus
    // re-checking support)
    // UPDATE_INVISIBLE (32) - suppresses render propagation to surrounding blocks
    private static final int SILENT_PLACE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE
            | Block.UPDATE_INVISIBLE;

    private static Block[] BALLOON_BLOCKS = null;

    private static boolean isChunkLoaded(ServerLevel level, int blockX, int blockZ) {
        return level.isLoaded(new BlockPos(blockX, 64, blockZ));
    }

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

    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos, int chunkX, int chunkZ) {
        // Try each of the 4 cardinal directions, walking outward 2–5 blocks
        // Stop the moment we'd leave the chunk boundary
        int chunkMinX = chunkX; // already the min block X
        int chunkMinZ = chunkZ;
        int chunkMaxX = chunkX + 15;
        int chunkMaxZ = chunkZ + 15;

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] dir : directions) {
            for (int dist = 2; dist <= 5; dist++) {
                int x = balloonPos.getX() + dir[0] * dist;
                int z = balloonPos.getZ() + dir[1] * dist;

                // Stop walking this direction if we've left the chunk
                if (x < chunkMinX || x > chunkMaxX || z < chunkMinZ || z > chunkMaxZ)
                    break;

                int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                if (groundY > level.getMinBuildHeight()) {
                    return new BlockPos(x, groundY, z);
                }
            }
        }

        // Fallback: directly under the balloon
        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, balloonPos.getX(), balloonPos.getZ());
        if (groundY > level.getMinBuildHeight())
            return new BlockPos(balloonPos.getX(), groundY, balloonPos.getZ());

        return null;
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel))
            return;
        if (serverLevel.dimension() != Level.END)
            return;
        if (event.isNewChunk()) {
            if (!(event.getChunk() instanceof LevelChunk chunk))
                return;
            if (serverLevel.getRandom().nextInt(100) >= SPAWN_CHANCE)
                return;

            final int chunkX = chunk.getPos().getMinBlockX();
            final int chunkZ = chunk.getPos().getMinBlockZ();

            // Defer ALL block reading/writing to the next server tick.

            serverLevel.getServer().execute(() -> {
                try {
                    doSpawn(serverLevel, chunkX, chunkZ);
                } catch (Exception e) {
                    JustaCreature.LOGGER.error("[JustACreature] EnderManiacSpawnHandler failed: {}", e.getMessage());
                }
            });
        }

    }

    private static void doSpawn(ServerLevel serverLevel, int chunkX, int chunkZ) {
        int minY = 60;
        int maxY = 90;

        BlockPos balloonPos = null;
        for (int probe = 0; probe < MAX_PROBES; probe++) {
            int rx = chunkX + serverLevel.getRandom().nextInt(16);
            int rz = chunkZ + serverLevel.getRandom().nextInt(16);

            for (int y = maxY - 1; y > minY; y--) {
                BlockPos pos = new BlockPos(rx, y, rz);
                Block b = serverLevel.getBlockState(pos).getBlock();
                if ((b == Blocks.CHORUS_PLANT || b == Blocks.CHORUS_FLOWER)
                        && serverLevel.getBlockState(pos.below()).isAir()) {
                    balloonPos = pos.below();
                    break;
                }
            }
            if (balloonPos != null)
                break;
        }

        if (balloonPos == null)
            return;

        Block[] balloons = getBalloonBlocks();
        Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
        serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), SILENT_PLACE_FLAGS);

        BlockPos spawnPos = findGroundNear(serverLevel, balloonPos, chunkX, chunkZ);
        if (spawnPos == null)
            return;

        // Guard: ensure the spawn chunk is fully loaded and ticket-level is valid
        // getChunkSource().getChunk() returns null if the chunk isn't ready
        net.minecraft.world.level.chunk.LevelChunk spawnChunk = serverLevel
                .getChunkSource()
                .getChunk(spawnPos.getX() >> 4, spawnPos.getZ() >> 4, false);
        if (spawnChunk == null)
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
}