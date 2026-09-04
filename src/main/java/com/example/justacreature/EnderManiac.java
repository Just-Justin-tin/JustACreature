package com.example.justacreature;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        // Listen for changes to the IS_REVEALED variable
        if (IS_REVEALED.equals(key)) {
            if (this.level().isClientSide()) {
                if (this.entityData.get(IS_REVEALED)) {
                    // Start the animation
                    this.revealAnimationState.start(this.tickCount);
                } else {
                    // Stop the animation
                    this.revealAnimationState.stop();
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

        if (this.level().isClientSide()) {
            if (this.getEntityData().get(IS_REVEALED)) {
                this.revealAnimationState.startIfStopped(this.tickCount);
                super.playSound(com.example.justacreature.JustaCreature.ENDERMANIAC_REVEAL.get(), 1.0F, 1.0F);
            } else {
                this.revealAnimationState.stop();
            }
        }
    }
}