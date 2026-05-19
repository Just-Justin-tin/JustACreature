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
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Creeper;

public class BalloonCreeper extends Creeper {
    // This now stores an ARGB color code (like 0xFFFF0000 for pure red)
    private static final EntityDataAccessor<Integer> BALLOON_COLOR = SynchedEntityData.defineId(BalloonCreeper.class,
            EntityDataSerializers.INT);

    public BalloonCreeper(EntityType<? extends Creeper> type, Level level) {
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

    @Override
    protected void registerGoals() {
        // IMPORTANT: We do NOT call super.registerGoals() here.
        // This wipes out all the normal Wolf behaviors (like begging for bones or
        // sitting).
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);

        // 1. Movement and Action Goals
        this.goalSelector.addGoal(1, new SwellGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 1.0F)); // Makes them jump at you!
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false)); // Run at target and hit them
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wander when bored
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // 2. Targeting Goals (Who should they attack?)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}