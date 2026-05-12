package com.example.justacreature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Ghast;

public class BalloonGhast extends Ghast {
    // This now stores an ARGB color code (like 0xFFFF0000 for pure red)
    private static final EntityDataAccessor<Integer> BALLOON_COLOR = SynchedEntityData.defineId(BalloonGhast.class,
            EntityDataSerializers.INT);

    public BalloonGhast(EntityType<? extends Ghast> type, Level level) {
        super(type, level);
    }

    public boolean nextShotIsWindCharge = false;

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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("BalloonColor", this.getBalloonColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BalloonColor")) {
            this.entityData.set(BALLOON_COLOR, compound.getInt("BalloonColor"));
        }
    }

}