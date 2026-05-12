package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import java.util.EnumSet;

public class SleepNearLavaGoal extends Goal {
    private final CreatureBosambo bosambo;
    private BlockPos targetLavaPos = null;
    private BlockPos targetSleepPos = null;
    private int layDownTimer = 0;
    private int totalSleepTime = 0;
    private int walkingTimeout = 0;
    private int standUpTimer = 0;

    public SleepNearLavaGoal(CreatureBosambo bosambo) {
        this.bosambo = bosambo;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    private boolean isAreaClear(Level level, BlockPos targetPos) {
        BlockPos corner1 = targetPos.offset(-1, 1, -1);
        BlockPos corner2 = targetPos.offset(1, 3, 1);

        for (BlockPos currentPos : BlockPos.betweenClosed(corner1, corner2)) {
            if (currentPos.equals(targetPos)) {
                continue;
            }
            BlockState state = level.getBlockState(currentPos);
            // Allow the mob to sleep in tall grass/flowers, but not inside solid blocks
            if (!state.isAir() && !state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    private BlockPos findClearSleepSpotNear(Level level, BlockPos lavaPos) {
        // Expand the search box horizontally, but restrict it to the exact Y-level of
        // the lava
        BlockPos corner1 = lavaPos.offset(-16, 0, -16);
        BlockPos corner2 = lavaPos.offset(16, 0, 16);

        for (BlockPos floorPos : BlockPos.betweenClosed(corner1, corner2)) {

            // STRICT Y-LEVEL CHECK: The floor MUST be at the exact same Y as the lava pool
            // This guarantees the mob's feet will be exactly 1 block above the lava!
            if (floorPos.getY() != lavaPos.getY()) {
                continue;
            }

            double distSq = floorPos.distToCenterSqr(lavaPos.getCenter());

            // Look for spots between 4 and 5 blocks away (distSq 16.0 to 25.0)
            if (distSq >= 16.0D && distSq <= 25.0D) {

                if (!level.isEmptyBlock(floorPos) && !level.getFluidState(floorPos).is(FluidTags.LAVA)) {

                    boolean hasLavaNearby = false;
                    // Check a strict 2-block radius in all directions around the sleep spot
                    BlockPos safeCorner1 = floorPos.offset(-2, -1, -2);
                    BlockPos safeCorner2 = floorPos.offset(2, 2, 2);

                    for (BlockPos currentPos : BlockPos.betweenClosed(safeCorner1, safeCorner2)) {
                        if (level.getFluidState(currentPos).is(FluidTags.LAVA)) {
                            hasLavaNearby = true;
                            break;
                        }
                    }

                    if (!hasLavaNearby && isAreaClear(level, floorPos)) {
                        return floorPos.immutable();
                    }
                }
            }
        }
        return null;
    }

    private boolean findTargetLocations(Level level, BlockPos center) {
        double closestDistance = Double.MAX_VALUE;
        BlockPos corner1 = center.offset(-16, -4, -16);
        BlockPos corner2 = center.offset(16, 4, 16);

        for (BlockPos pos : BlockPos.betweenClosed(corner1, corner2)) {
            while (level.getFluidState(pos).is(FluidTags.LAVA) && level.getFluidState(pos.above()).is(FluidTags.LAVA)) {
                pos = pos.above();
            }

            if (level.getFluidState(pos).is(FluidTags.LAVA) && level.getBlockState(pos.above()).isAir()) {
                double distance = center.distSqr(pos);
                if (distance <= closestDistance) {
                    BlockPos sleepSpot = findClearSleepSpotNear(level, pos);
                    if (sleepSpot != null) {
                        closestDistance = distance;
                        this.targetLavaPos = pos.immutable();
                        this.targetSleepPos = sleepSpot;
                    }
                }
            }
        }
        return this.targetLavaPos != null && this.targetSleepPos != null;
    }

    @Override
    public boolean canUse() {
        if (this.bosambo.getRandom().nextInt(200) != 0) {
            return false;
        }
        if (this.bosambo.isDigging()) {
            return false;
        }

        this.targetLavaPos = null;
        this.targetSleepPos = null;

        return findTargetLocations(this.bosambo.level(), this.bosambo.blockPosition());
    }

    @Override
    public boolean canContinueToUse() {
        if (this.walkingTimeout > 200) {
            return false;
        }
        return this.standUpTimer < 60;
    }

    @Override
    public void start() {
        this.layDownTimer = 0;
        this.totalSleepTime = 0;
        this.walkingTimeout = 0;
        this.standUpTimer = 0;
        this.bosambo.getNavigation().moveTo(targetSleepPos.getX(), targetSleepPos.getY() + 1, targetSleepPos.getZ(),
                1.0D);
    }

    @Override
    public void tick() {
        double distanceToSleepSpot = this.bosambo.distanceToSqr(this.targetSleepPos.getCenter().add(0, 1, 0));

        // RE-ADDED YOUR STRICT HEIGHT CHECK HERE
        boolean isExactlyOneBlockAbove = this.bosambo.getBlockY() == (this.targetLavaPos.getY() + 1);
        boolean hasArrivedLocational = distanceToSleepSpot <= 1.5D && isExactlyOneBlockAbove;

        // Trigger if we arrived safely, OR if the animation is already playing
        if (hasArrivedLocational || this.layDownTimer > 0 || this.totalSleepTime > 0) {
            this.bosambo.getNavigation().stop();

            // Turn to face the lava pool while sleeping
            double dX = this.targetLavaPos.getX() + 0.5D - this.bosambo.getX();
            double dZ = this.targetLavaPos.getZ() + 0.5D - this.bosambo.getZ();
            float exactYaw = (float) (Math.atan2(dZ, dX) * (180D / Math.PI)) - 90.0F;
            this.bosambo.setYRot(exactYaw);
            this.bosambo.yBodyRot = exactYaw;
            this.bosambo.yHeadRot = exactYaw;

            if (layDownTimer < 60) {
                this.bosambo.setLaying(true);
                this.bosambo.setSleeping(false);

                if (layDownTimer == 28) {
                    if (this.bosambo.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        serverLevel.playSound(null, this.bosambo.blockPosition(),
                                net.minecraft.sounds.SoundEvents.DEEPSLATE_BREAK,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.5F, 0.2F);
                        serverLevel.playSound(null, this.bosambo.blockPosition(),
                                net.minecraft.sounds.SoundEvents.GRAVEL_BREAK,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.5F, 0.4F);

                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                                this.bosambo.getX(), this.bosambo.getY() + 0.5D, this.bosambo.getZ(),
                                40, 1.5D, 0.2D, 1.5D, 0.05D);
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                                this.bosambo.getX(), this.bosambo.getY() + 0.5D, this.bosambo.getZ(),
                                15, 1.5D, 0.2D, 1.5D, 0.05D);
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA,
                                this.bosambo.getX(), this.bosambo.getY() + 0.5D, this.bosambo.getZ(),
                                15, 1.0D, 0.2D, 1.0D, 0.0D);
                    }
                }
                layDownTimer++;
            } // PHASE 2: Sleeping (60 to 600 ticks)
            else if (totalSleepTime < 600) {
                this.bosambo.setLaying(false);
                this.bosambo.setSleeping(true);
                this.bosambo.setStanding(false);

                totalSleepTime++;
            }
            // PHASE 3: Standing Up (Wait for the stand animation to finish)
            else if (standUpTimer < 60) {
                this.bosambo.setLaying(false);
                this.bosambo.setSleeping(false);
                this.bosambo.setStanding(true);

                standUpTimer++; // We need to count how long he has been standing up!
            }
        } else {
            // Keep walking to the specific spot if we aren't there yet
            if (!this.bosambo.getNavigation().isInProgress()) {
                this.bosambo.getNavigation().moveTo(targetSleepPos.getX(), targetSleepPos.getY() + 1,
                        targetSleepPos.getZ(), 1.0D);
                if (!this.bosambo.getNavigation().isInProgress()) {
                    this.walkingTimeout += 10;
                }
            }
        }
    }

    @Override
    public void stop() {
        this.bosambo.setLaying(false);
        this.bosambo.setSleeping(false);
        this.bosambo.setStanding(false);
        this.targetLavaPos = null;
        this.targetSleepPos = null;
    }
}