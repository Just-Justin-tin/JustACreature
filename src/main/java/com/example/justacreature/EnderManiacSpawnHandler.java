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

            // Pick a random X/Z inside this chunk and scan that ONE column for chorus
            int rx = chunkX + serverLevel.getRandom().nextInt(16);
            int rz = chunkZ + serverLevel.getRandom().nextInt(16);

            // Find the LOWEST chorus block in this column
            BlockPos lowestChorus = null;
            for (int y = minY; y < maxY; y++) {
                BlockPos pos = new BlockPos(rx, y, rz);
                Block b = serverLevel.getBlockState(pos).getBlock();
                if (b == Blocks.CHORUS_PLANT || b == Blocks.CHORUS_FLOWER) {
                    lowestChorus = pos; // keep overwriting — last one found scanning upward is lowest
                    break; // scanning bottom-up so first hit IS the lowest
                }
            }

            // No chorus in this column — bail out cheaply
            if (lowestChorus == null) return;

            // Place balloon ONE block BELOW the lowest chorus block
            BlockPos balloonPos = lowestChorus.below();
            if (balloonPos.getY() < minY) return;
            if (!serverLevel.getBlockState(balloonPos).isAir()) return;

            Block[] balloons = getBalloonBlocks();
            Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
            serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), Block.UPDATE_ALL);

            // Pick a random spot near the balloon and drop down to ground to spawn the mob
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
            JustaCreature.LOGGER.error("[JustACreature] EnderManiacSpawnHandler failed on chunk load: {}", e.getMessage());
        }
    }

    /**
     * Picks a random X/Z offset near the balloon, then drops straight down
     * until it finds a block that is air with solid ground directly below it.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int offsetX = level.getRandom().nextIntBetweenInclusive(-4, 4);
        int offsetZ = level.getRandom().nextIntBetweenInclusive(-4, 4);
        BlockPos.MutableBlockPos candidate = balloonPos.offset(offsetX, 0, offsetZ).mutable();

        int minY = level.getMinBuildHeight();
        while (candidate.getY() > minY + 1) {
            if (level.getBlockState(candidate).isAir()
                    && !level.getBlockState(candidate.below()).isAir()) {
                return candidate.immutable();
            }
            candidate.move(0, -1, 0);
        }
        return null;
    }
}
