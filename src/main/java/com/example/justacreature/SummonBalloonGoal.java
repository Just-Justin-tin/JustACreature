package com.example.justacreature;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;

public class SummonBalloonGoal extends Goal {
    private final blue_Endermaniac endermaniac;
    private int cooldown = 0;

    // 1. The list of random mobs to summon. You can add any entity type here!
    private final List<EntityType<? extends LivingEntity>> balloonTypes = List.of(
            EntityType.ZOMBIE, EntityType.CREEPER, EntityType.WOLF, EntityType.BEE, EntityType.GHAST);
            
    public SummonBalloonGoal(blue_Endermaniac endermaniac) {
        this.endermaniac = endermaniac;
        // This tells the AI to stop walking while it summons
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.endermaniac.getTarget();
        // Only use this goal if the Endermaniac has a target and it's within 16 blocks
        return target != null && target.isAlive() && this.endermaniac.distanceToSqr(target) < 256.0D;
    }

    @Override
    public void tick() {
        LivingEntity target = this.endermaniac.getTarget();
        if (target == null)
            return;

        this.endermaniac.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        // Time to summon!
        summonBalloonMob(target);

        // Set cooldown to 100 ticks (5 seconds) before it summons again
        this.cooldown = 100;
    }

    private void summonBalloonMob(LivingEntity target) {
        Level level = this.endermaniac.level();
        if (level.isClientSide())
            return;

        // 2. Pick a random mob type from the list
        EntityType<? extends LivingEntity> typeToSummon = balloonTypes
                .get(this.endermaniac.getRandom().nextInt(balloonTypes.size()));
        LivingEntity balloonMob = typeToSummon.create(level);

        if (balloonMob != null) {
            // Spawn it slightly randomly around the Endermaniac
            balloonMob.moveTo(this.endermaniac.getX() + (level.random.nextDouble() - 0.5D) * 3,
                    this.endermaniac.getY(),
                    this.endermaniac.getZ() + (level.random.nextDouble() - 0.5D) * 3,
                    this.endermaniac.getYRot(), 0.0F);

            // 3. Make it "Pop" in one hit by setting its max health to 1 (half a heart)
            if (balloonMob.getAttribute(Attributes.MAX_HEALTH) != null) {
                balloonMob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0D);
                balloonMob.setHealth(1.0F);
            }

            // 4. Give it a special tag so we know it's a balloon!
            balloonMob.addTag("is_balloon_mob");

            // Make the summoned mob instantly target the player
            if (balloonMob instanceof Mob mob) {
                mob.setTarget(target);
            }

            level.addFreshEntity(balloonMob);

            // Play a fun "pop" or wool sound when summoned
            level.playSound(null, this.endermaniac.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.HOSTILE, 1.0f,
                    1.0f);

            // Add some poof particles
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, balloonMob.getX(), balloonMob.getY() + 1.0D,
                        balloonMob.getZ(), 10, 0.2D, 0.2D, 0.2D, 0.05D);
            }
        }
    }
}