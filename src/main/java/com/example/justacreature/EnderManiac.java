package com.example.justacreature;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;

public class EnderManiac extends Monster {

    private static final EntityDataAccessor<Boolean> IS_REVEALING = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_DISGUISED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_REVEALED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState revealAnimationState = new AnimationState();

    private BlockPos balloonPos;

    private int AnimationTimer = -1;

    public void setBalloonPos(BlockPos pos) {
        this.balloonPos = pos;
    }

    public BlockPos getBalloonPos() {
        return this.balloonPos;
    }

    public void setRevealed(boolean revealed) {
        this.entityData.set(IS_REVEALING, revealed);
    }

    public void setDisguised(boolean disguised) {
        this.entityData.set(IS_DISGUISED, disguised);
    }

    public void setRevealing(boolean isRevealed) {
        this.entityData.set(HAS_REVEALED, isRevealed);
    }

    public boolean isRevealing() {
        return this.entityData.get(IS_REVEALING);
    }

    public boolean isDisguised() {
        return this.entityData.get(IS_DISGUISED);
    }

    public boolean isRevealed() {
        return this.entityData.get(HAS_REVEALED);
    }

    public EnderManiac(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_REVEALING, false);
        builder.define(IS_DISGUISED, true);
        builder.define(HAS_REVEALED, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // If the mob takes damage on the server, cancel the animation
        if (!this.level().isClientSide() && this.entityData.get(IS_DISGUISED)) {
            this.setRevealing(true);
            this.setDisguised(false);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        // Listen for changes to the IS_REVEALING variable
        if (IS_REVEALING.equals(key)) {
            if (this.entityData.get(IS_REVEALING)) {
                this.AnimationTimer = 240; // 4.5 seconds at 20 ticks/sec
            } else {
                if (this.level().isClientSide()) {
                    this.revealAnimationState.stop();
                }
                this.AnimationTimer = -1; // cancel a pending sound if un-revealed early
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.balloonPos != null) {
            tag.putInt("BalloonX", this.balloonPos.getX());
            tag.putInt("BalloonY", this.balloonPos.getY());
            tag.putInt("BalloonZ", this.balloonPos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("BalloonX")) {
            this.balloonPos = new BlockPos(tag.getInt("BalloonX"), tag.getInt("BalloonY"), tag.getInt("BalloonZ"));
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
    public void tick() {
        super.tick();

        if (this.balloonPos != null) {
            this.getLookControl().setLookAt(
                    this.balloonPos.getX() + 0.5,
                    this.balloonPos.getY() + 0.5,
                    this.balloonPos.getZ() + 0.5,
                    30.0F, 30.0F);
        }
        if (this.getEntityData().get(IS_REVEALING) && this.AnimationTimer > 0) {
            if (this.AnimationTimer == 240 && !this.level().isClientSide()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        com.example.justacreature.JustaCreature.ENDERMANIAC_REVEAL.get(),
                        this.getSoundSource(), 1.0F, 1.0F);
            }
            this.AnimationTimer--;
        }

        if (this.AnimationTimer == 150 && this.level().isClientSide()) {
            this.setRevealed(true);
            this.setRevealing(true);
            this.revealAnimationState.startIfStopped(this.tickCount);

        }

        if (this.AnimationTimer == 100 && !this.level().isClientSide()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0D))
                        .forEach(player -> player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40, 0)));
                serverLevel.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0D))
                        .forEach(player -> player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0)));
            }
        }

        if (this.AnimationTimer == 0 && !this.level().isClientSide()) {
            this.remove(RemovalReason.DISCARDED);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENDERMAN_TELEPORT,
                    this.getSoundSource(), 1.0F, 1.0F);

            pink_Endermaniac pinkManiac = JustaCreatureMobs.PINK_ENDERMANIAC.get().create(this.level());
            pinkManiac.setPos(this.getX() - 10 + random.nextInt(20), this.getY(),
                    this.getZ() - 10 + random.nextInt(20));

            this.level().addFreshEntity(pinkManiac);
            blue_Endermaniac blueManiac = JustaCreatureMobs.BLUE_ENDERMANIAC.get().create(this.level());
            blueManiac.setPos(this.getX() - 10 + random.nextInt(20), this.getY() + 10,
                    this.getZ() - 10 + random.nextInt(20));

            this.level().addFreshEntity(blueManiac);
            red_Endermaniac redManiac = JustaCreatureMobs.RED_ENDERMANIAC.get().create(this.level());
            redManiac.setPos(this.getX() - 10 + random.nextInt(20), this.getY(), this.getZ() - 10 + random.nextInt(20));

            this.level().addFreshEntity(redManiac);

            this.AnimationTimer = -1;
        }
    }
}
