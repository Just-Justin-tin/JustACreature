package com.example.justacreature;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class EnderManiac extends Monster {

    private static final EntityDataAccessor<Boolean> IS_REVEALED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DISGUISED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState revealAnimationState = new AnimationState();

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
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.getEntityData().get(IS_REVEALED)) {
                this.revealAnimationState.start(this.tickCount);
            }
        }
    }
}
