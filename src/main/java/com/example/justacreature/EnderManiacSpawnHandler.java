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
    private static final int MAX_PROBES = 12;

    // Place the balloon with zero side effects on neighbors:
    //   UPDATE_CLIENTS     (2)  - syncs to clients so it renders
    //   UPDATE_KNOWN_SHAPE (16) - skips neighbor shape updates (stops chorus re-checking support)
    //   UPDATE_INVISIBLE   (32) - suppresses render propagation to surrounding blocks
    private static final int SILENT_PLACE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_INVISIBLE;

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
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (serverLevel.dimension() != Level.END) return;
        if (!event.isNewChunk()) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
        if (serverLevel.getRandom().nextInt(100) >= SPAWN_CHANCE) return;

        final int chunkX = chunk.getPos().getMinBlockX();
        final int chunkZ = chunk.getPos().getMinBlockZ();

        // Defer ALL block reading/writing to the next server tick.
        // This means the chunk load thread is never blocked, which stops the infinite save hang.
        serverLevel.getServer().execute(() -> {
            try {
                doSpawn(serverLevel, chunkX, chunkZ);
            } catch (Exception e) {
                JustaCreature.LOGGER.error("[JustACreature] EnderManiacSpawnHandler failed: {}", e.getMessage());
            }
        });
    }

    private static void doSpawn(ServerLevel serverLevel, int chunkX, int chunkZ) {
        int minY = 50;
        int maxY = 100;

        BlockPos balloonPos = null;
        for (int probe = 0; probe < MAX_PROBES; probe++) {
            int rx = chunkX + serverLevel.getRandom().nextInt(16);
            int rz = chunkZ + serverLevel.getRandom().nextInt(16);

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

        if (balloonPos == null) return;

        Block[] balloons = getBalloonBlocks();
        Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
        serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), SILENT_PLACE_FLAGS);

        BlockPos spawnPos = findGroundNear(serverLevel, balloonPos);
        if (spawnPos == null) return;

        EnderManiac mob = JustaCreatureMobs.ENDER_MANIAC.get().create(serverLevel);
        if (mob == null) return;
        mob.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                serverLevel.getRandom().nextFloat() * 360f, 0f);
        mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                net.minecraft.world.entity.MobSpawnType.CHUNK_GENERATION, null);
        serverLevel.addFreshEntity(mob);
    }

    /**
     * Finds a ground position at least 2 blocks away (horizontally) from the balloon.
     * Tries random offsets in the range [-5, -2] and [2, 5] on both axes,
     * then drops straight down until it finds air above a solid block.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int minY = level.getMinBuildHeight();

        // All offsets here have |x| >= 2 or |z| >= 2, guaranteeing minimum 2-block distance.
        int[][] offsets = {
                { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 },
                { 2, 2 }, { -2, 2 }, { 2, -2 }, { -2, -2 },
                { 3, 1 }, { -3, 1 }, { 1, 3 }, { -1, -3 },
                { 4, 0 }, { -4, 0 }, { 0, 4 }, { 0, -4 }
        };

        for (int[] off : offsets) {
            BlockPos.MutableBlockPos candidate = balloonPos.offset(off[0], 0, off[1]).mutable();
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
