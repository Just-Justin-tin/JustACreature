package com.example.justacreature;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class EnderManiac extends Monster {

    private static final EntityDataAccessor<Boolean> IS_REVEALED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_DISGUISED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState revealAnimationState = new AnimationState();

    private BlockPos balloonPos;

    private int revealSoundDelay = -1;

    public void setBalloonPos(BlockPos pos) {
        this.balloonPos = pos;
    }

    public BlockPos getBalloonPos() {
        return this.balloonPos;
    }

    public void setRevealed(boolean revealed) {
        this.entityData.set(IS_REVEALED, revealed);
    }

    public void setDisguised(boolean disguised) {
        this.entityData.set(IS_DISGUISED, disguised);
    }

    public boolean isRevealed() {
        return this.entityData.get(IS_REVEALED);
    }

    public boolean isDisguised() {
        return this.entityData.get(IS_DISGUISED);
    }

    public EnderManiac(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_REVEALED, false);
        builder.define(IS_DISGUISED, true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // If the mob takes damage on the server, cancel the animation
        if (!this.level().isClientSide() && this.entityData.get(IS_DISGUISED)) {
            this.setRevealed(true);
            this.setDisguised(false);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        // Listen for changes to the IS_REVEALED variable
        if (IS_REVEALED.equals(key)) {
            if (this.entityData.get(IS_REVEALED)) {
                if (this.level().isClientSide()) {
                    this.revealAnimationState.start(this.tickCount);
                } else {
                    this.revealSoundDelay = 90; // 4.5 seconds at 20 ticks/sec
                }
            } else {
                if (this.level().isClientSide()) {
                    this.revealAnimationState.stop();
                } else {
                    this.revealSoundDelay = -1; // cancel a pending sound if un-revealed early
                }
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
    public void tick() {
        super.tick();

        if (this.balloonPos != null) {
            this.getLookControl().setLookAt(
                    this.balloonPos.getX() + 0.5,
                    this.balloonPos.getY() + 0.5,
                    this.balloonPos.getZ() + 0.5,
                    30.0F, 30.0F);
        }
        if (this.getEntityData().get(IS_REVEALED)) {

            if (!this.level().isClientSide() && this.revealSoundDelay > 0) {
                if (this.revealSoundDelay == 90) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            com.example.justacreature.JustaCreature.ENDERMANIAC_REVEAL.get(),
                            this.getSoundSource(), 1.0F, 1.0F);
                }
                this.revealSoundDelay--;
            }

            if (this.revealSoundDelay == 0) {
                this.revealAnimationState.startIfStopped(this.tickCount);
            } else {
                this.revealAnimationState.stop();
            }
        }
    }
}