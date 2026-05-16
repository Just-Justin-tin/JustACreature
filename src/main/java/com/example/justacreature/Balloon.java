package com.example.justacreature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Balloon extends Entity {

    private static final EntityDataAccessor<Integer> BALLOON_COLOR = SynchedEntityData.defineId(Balloon.class,
            EntityDataSerializers.INT);

    public final AnimationState driftAnimationState = new AnimationState();

    public Balloon(EntityType<Balloon> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        // 1. Pick random RGB values between 0 and 255
        int r = this.random.nextInt(0, 256);
        int g = this.random.nextInt(0, 256);
        int b = this.random.nextInt(0, 256);

        // 2. Combine them into a single ARGB integer (255 on the left means 100% solid
        // opacity)
        int color = (255 << 24) | (r << 16) | (g << 8) | b;

        builder.define(BALLOON_COLOR, color);
    }

    public int getBalloonColor() {
        return this.entityData.get(BALLOON_COLOR);
    }

    @Override
    public void tick() {
        super.tick();
        // 2. Play the flying animation continuously while it is in the air!
        if (this.level().isClientSide()) {
            this.driftAnimationState.startIfStopped(this.tickCount);
        }
    }
}
