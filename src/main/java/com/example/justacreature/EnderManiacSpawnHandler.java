package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
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

    // All 16 balloon block variants to pick from randomly
    private static final Block[] BALLOON_BLOCKS = {
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

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        // Only run on server side in The End
        if (!(event.getLevel() instanceof ServerLevel serverLevel))
            return;
        if (serverLevel.dimension() != Level.END)
            return;
        // Only process newly generated chunks, not every load
        if (!event.isNewChunk())
            return;
        if (!(event.getChunk() instanceof LevelChunk chunk))
            return;

        List<BlockPos> chorusPositions = new ArrayList<>();

        // Scan the chunk for chorus plants and chorus flowers
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

        if (chorusPositions.isEmpty())
            return;

        BlockPos chorusPos = chorusPositions.get(serverLevel.getRandom().nextInt(chorusPositions.size()));

        if (serverLevel.getRandom().nextInt(chorusPositions.size()) == 1) {

            // Find the block directly below the chorus plant/flower
            BlockPos balloonPos = findPositionBelowChorus(serverLevel, chorusPos);
            if (balloonPos == null)
                return;

            // Only place if the spot is air
            if (!serverLevel.getBlockState(balloonPos).isAir())
                return;

            // Pick a random balloon color
            Block randomBalloon = BALLOON_BLOCKS[serverLevel.getRandom().nextInt(BALLOON_BLOCKS.length)];
            serverLevel.setBlock(balloonPos, randomBalloon.defaultBlockState(), Block.UPDATE_ALL);

            // Find ground level near the balloon to spawn the EnderManiac
            BlockPos spawnPos = findGroundNear(serverLevel, balloonPos);
            if (spawnPos == null)
                return;

            // Spawn the EnderManiac
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

    /**
     * Walks downward from the chorus pos to find the first air block below it
     * that has a solid block beneath it (so the balloon floats just under the
     * plant).
     */
    private static BlockPos findPositionBelowChorus(ServerLevel level, BlockPos chorusPos) {
        // Walk down from the chorus block until we find air sitting on a solid block
        BlockPos current = chorusPos.below();
        for (int i = 0; i < 20; i++) {
            if (level.getBlockState(current).isAir()) {
                BlockPos below = current.below();
                // If there's solid ground below this air, place the balloon here
                if (!level.getBlockState(below).isAir()) {
                    return current;
                }
            }
            current = current.below();
        }
        return null;
    }

    /**
     * Finds a solid ground position within a few blocks of the balloon,
     * offset randomly so the EnderManiac spawns near (not on) the balloon.
     */
    private static BlockPos findGroundNear(ServerLevel level, BlockPos balloonPos) {
        int offsetX = level.getRandom().nextIntBetweenInclusive(-4, 4);
        int offsetZ = level.getRandom().nextIntBetweenInclusive(-4, 4);
        BlockPos searchStart = balloonPos.offset(offsetX, 0, offsetZ);

        // Scan downward to find the ground
        for (int dy = 0; dy > -30; dy--) {
            BlockPos candidate = searchStart.offset(0, dy, 0);
            BlockPos below = candidate.below();
            if (level.getBlockState(candidate).isAir() && !level.getBlockState(below).isAir()) {
                return candidate;
            }
        }
        return null;
    }
}
