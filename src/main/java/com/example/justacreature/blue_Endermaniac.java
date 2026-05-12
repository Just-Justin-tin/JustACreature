package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// 1. Extend Monster instead of EnderMan
public class blue_Endermaniac extends Monster {
    public final AnimationState walkAnimationState = new AnimationState();

    public blue_Endermaniac(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        // 2. Give the mob 3D flight controls!
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double oldX = this.getX();
            double oldY = this.getY();
            double oldZ = this.getZ();

            // Pick a random 3D spot nearby (up to 16 blocks away in any direction)
            double newX = oldX + (this.random.nextDouble() - 0.5D) * 32.0D;
            double newY = oldY + (this.random.nextDouble() - 0.5D) * 32.0D;
            double newZ = oldZ + (this.random.nextDouble() - 0.5D) * 32.0D;

            // Don't let it teleport out of the world boundaries
            if (newY < this.level().getMinBuildHeight() || newY >= this.level().getMaxBuildHeight()) {
                return false;
            }

            // Attempt to move there instantly
            this.teleportTo(newX, newY, newZ);

            // Check if the new spot is clear of solid blocks (so it doesn't get stuck in a
            // wall)
            if (this.level().noCollision(this)) {
                // Play the classic "Vwoop!" teleport sound at the OLD location
                this.level().playSound(null, oldX, oldY, oldZ, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(),
                        1.0F, 1.0F);
                // Play the sound at the NEW location
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                return true;
            } else {
                // If it accidentally teleported inside a wall, revert the teleport!
                this.teleportTo(oldX, oldY, oldZ);
                return false;
            }
        }
        return false;
    }

    // 3. Give the mob a 3D pathfinding map
    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingNav = new FlyingPathNavigation(this, level);
        flyingNav.setCanOpenDoors(false);
        flyingNav.setCanFloat(true);
        return flyingNav;
    }

    // 4. Add flying AI goals
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D)); // Wander around in the air!
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    // 5. Turn off fall damage (so it doesn't die if it lands)
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // Leave empty so it ignores fall calculations
    }

    // 6. Update the animation trigger for 3D space
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            // Check 3D movement (lengthSqr) instead of horizontal movement!
            boolean isMoving = this.getDeltaMovement().lengthSqr() > 1.0E-6D;

            if (isMoving) {
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
            }
        }
    }

    @Override
    public void aiStep() {
        // We only want to spawn graphics on the client side!
        if (this.level().isClientSide()) {
            // Spawn 2 particles every single tick
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(
                        ParticleTypes.PORTAL,
                        this.getRandomX(0.5D), // Random X position around the mob
                        this.getRandomY() - 0.25D, // Slightly below the center so they float up
                        this.getRandomZ(0.5D), // Random Z position
                        (this.random.nextDouble() - 0.5D) * 2.0D, // Random X drift speed
                        -this.random.nextDouble(), // Drift downwards (portals float down and fade)
                        (this.random.nextDouble() - 0.5D) * 2.0D // Random Z drift speed
                );
            }
        }

        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // 1. Dodge Arrows/Projectiles completely
        if (source.getDirectEntity() instanceof Projectile) {
            for (int i = 0; i < 64; ++i) { // Try to find a safe teleport spot up to 64 times
                if (this.teleport()) {
                    return false; // Successfully dodged, take 0 damage
                }
            }
            return false; // Still take 0 damage even if it couldn't find a spot to teleport
        }

        // 2. Take normal damage from swords/melee
        boolean wasHurt = super.hurt(source, amount);

        // 3. Have a 25% chance to teleport away after getting hit by a sword
        if (wasHurt && !this.level().isClientSide() && this.random.nextInt(4) == 0) {
            this.teleport();
        }

        return wasHurt;
    }

}