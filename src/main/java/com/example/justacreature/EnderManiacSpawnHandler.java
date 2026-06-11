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

    // 10% chance per newly generated End chunk that has chorus plants/flowers
    private static final int SPAWN_CHANCE = 10;

    // Lazily initialized — populated on first use, after registries are ready
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

        // Roll the chance first — cheap exit before scanning
        if (serverLevel.getRandom().nextInt(100) >= SPAWN_CHANCE) return;

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
                    if (block == Blocks.CHORUS_PLANT || block == Blocks.CHORUS_FLOWER) {
                        chorusPositions.add(mutable.immutable());
                    }
                }
            }
        }

        // No chorus in this chunk — nothing to do
        if (chorusPositions.isEmpty()) return;

        // Pick ONE random chorus position from the list
        BlockPos chorusPos = chorusPositions.get(serverLevel.getRandom().nextInt(chorusPositions.size()));

        // Find a free air spot just below the chorus structure
        BlockPos balloonPos = findPositionBelowChorus(serverLevel, chorusPos);
        if (balloonPos == null) return;
        if (!serverLevel.getBlockState(balloonPos).isAir()) return;

        // Place a random balloon color
        Block[] balloons = getBalloonBlocks();
        Block randomBalloon = balloons[serverLevel.getRandom().nextInt(balloons.length)];
        serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), Block.UPDATE_ALL);

        // Find ground nearby and spawn exactly ONE EnderManiac
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
     * Walks downward from the chorus pos to find the first air block sitting
     * directly above solid ground (so the balloon appears just under the plant).
     */
    private static BlockPos findPositionBelowChorus(ServerLevel level, BlockPos chorusPos) {
        BlockPos current = chorusPos.below();
        for (int i = 0; i < 20; i++) {
            if (level.getBlockState(current).isAir()) {
                if (!level.getBlockState(current.below()).isAir()) {
                    return current;
                }
            }
            current = current.below();
        }
        return null;
    }

    /**
     * Finds a solid ground position within ±4 blocks of the balloon,
     * so the EnderManiac spawns nearby on the ground rather than on the balloon.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int offsetX = level.getRandom().nextIntBetweenInclusive(-4, 4);
        int offsetZ = level.getRandom().nextIntBetweenInclusive(-4, 4);
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
