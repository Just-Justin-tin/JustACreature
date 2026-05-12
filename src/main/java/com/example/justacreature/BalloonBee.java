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
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BalloonBee extends Bee {
    // This now stores an ARGB color code (like 0xFFFF0000 for pure red)
    private static final EntityDataAccessor<Integer> BALLOON_COLOR = SynchedEntityData.defineId(BalloonBee.class,
            EntityDataSerializers.INT);

    private static final MobEffect[] POSSIBLE_EFFECTS = new MobEffect[] {
            MobEffects.CONFUSION,
            MobEffects.BLINDNESS,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DARKNESS,
            MobEffects.LEVITATION
    };

    public BalloonBee(EntityType<? extends Bee> type, Level level) {
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
        // 1. We MUST call super first! The vanilla Bee expects its internal hive/pollen
        // goals to exist.
        // If we skip this, it throws a NullPointerException and crashes instantly.
        super.registerGoals();

        // 2. Now that the internal variables are safe, we wipe the actual behavior
        // lists clean
        // so it acts like a mindless hostile balloon, not a normal bee.
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);

        // 3. Movement and Action Goals (Notice we use Flying goals now!)
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true)); // Run/Fly at target
        // IMPORTANT: Use WaterAvoidingRandomFlyingGoal so it stays in the air!
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // 4. Targeting Goals (Who should they attack?)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // We use the base mob attack damage (skipping the vanilla Bee's poison)
        boolean hitSuccessfully = target.hurt(this.damageSources().mobAttack(this),
                (float) this.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE));

        if (hitSuccessfully && target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(
                    new MobEffectInstance(POSSIBLE_EFFECTS[random.nextInt(POSSIBLE_EFFECTS.length)], 100, 0), this);
        }

        return hitSuccessfully;
    }
}