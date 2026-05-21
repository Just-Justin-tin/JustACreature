package com.example.justacreature;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class pink_Endermaniac extends Monster {

    public final AnimationState walkAnimationState = new AnimationState();

    public pink_Endermaniac(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false)); // Chases and punches
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double oldX = this.getX();
            double oldY = this.getY();
            double oldZ = this.getZ();

            // Pick a random 3D spot nearby (up to 16 blocks away in any direction)
            double newX = oldX + (this.random.nextDouble() - 0.5D) * 32.0D;
            double newY = oldY + (this.random.nextDouble() - 0.5D) * 8.0D;
            double newZ = oldZ + (this.random.nextDouble() - 0.5D) * 32.0D;

            // Don't let it teleport out of the world boundaries
            if (newY < this.level().getMinBuildHeight() || newY >= this.level().getMaxBuildHeight()) {
                return false;
            }

            net.minecraft.core.BlockPos.MutableBlockPos mutablePos = new net.minecraft.core.BlockPos.MutableBlockPos(
                    newX, newY, newZ);

            // Move down until we hit a block that is NOT empty (i.e., solid ground)
            while (mutablePos.getY() > this.level().getMinBuildHeight() && this.level().isEmptyBlock(mutablePos)) {
                mutablePos.move(net.minecraft.core.Direction.DOWN);
            }

            // If it fell all the way out of the world, cancel teleport
            if (mutablePos.getY() <= this.level().getMinBuildHeight()) {
                return false;
            }

            // Move back up one block so the mob stands ON the ground, not inside it
            mutablePos.move(net.minecraft.core.Direction.UP);

            // Attempt to move there instantly using the corrected Y coordinate
            this.teleportTo(newX, mutablePos.getY(), newZ);

            // Check if the new spot is clear of solid blocks (so it doesn't get stuck in a
            // wall)
            if (this.level().noCollision(this)) {
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

    // 2. Check for movement every tick
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            // Check if the entity is moving horizontally
            boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;

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

        // 3. Have a 50% chance to teleport away between 1 and 4 times after getting hit
        // by a sword
        if (wasHurt && !this.level().isClientSide() && this.random.nextInt(2) == 0) {
            for (int i = 0; i < this.random.nextInt(5); i++) {
                this.teleport();
            }
        }

        return wasHurt;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hitSuccessfully = super.doHurtTarget(target);
        // Only spawn entities on the server side
        if (hitSuccessfully && !this.level().isClientSide() && this.random.nextInt(3) == 0) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            // 1. Create an instance of your custom slime entity
            BouncyBall newSlime = JustaCreatureMobs.BOUNCY_BALL.get().create(serverLevel);

            if (newSlime != null) {
                // 2. Set the spawn position (e.g., at the current mob's location)
                newSlime.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(),
                        this.getXRot());

                // 3. Randomize the size to either 1 (Small) or 2 (Medium)
                int slimeSize = this.getRandom().nextBoolean() ? 1 : 2;

                // Apply the size. The second parameter 'true' recalculates max health based on
                // the size.
                newSlime.setSize(slimeSize, true);

                // 4. Finally, spawn the entity into the world
                serverLevel.addFreshEntity(newSlime);
            }
        }
        return hitSuccessfully;
    }
}
