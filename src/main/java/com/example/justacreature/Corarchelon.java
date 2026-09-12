package com.example.justacreature;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.entity.animal.Animal;

public class Corarchelon extends Animal {

    public Corarchelon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    private static final float BABY_SCALE = 0.3F;
    private static final EntityDimensions BABY_DIMENSIONS;

    private static final EntityDataAccessor<Boolean> IS_EATING = SynchedEntityData
            .defineId(Corarchelon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SHAKING = SynchedEntityData
            .defineId(Corarchelon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LAYING = SynchedEntityData
            .defineId(Corarchelon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DIGGING_TICKS = SynchedEntityData
            .defineId(Corarchelon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_HAS_EGG = SynchedEntityData
            .defineId(Corarchelon.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState shakeAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var heldStack = player.getItemInHand(hand);

        if (heldStack.is(Items.SEA_PICKLE) && !this.isEating()) {
            if (!this.level().isClientSide()) {
                this.setEating(true);
                heldStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    static {
        BABY_DIMENSIONS = JustaCreatureMobs.CORARCHELON.get().getDimensions()
                .withAttachments(EntityAttachments.builder()
                        .attach(EntityAttachment.PASSENGER, 0.0F, JustaCreatureMobs.CORARCHELON.get().getHeight(),
                                -0.25F))
                .scale(0.3F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_EATING, false);
        builder.define(IS_SHAKING, false);
        builder.define(DIGGING_TICKS, 0);
        builder.define(IS_HAS_EGG, false);
    }

    public boolean isEating() {
        return this.entityData.get(IS_EATING);
    }

    public boolean isShaking() {
        return this.entityData.get(IS_SHAKING);
    }

    public boolean isLaying() {
        return this.entityData.get(IS_LAYING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(IS_EATING, eating);
    }

    public void setShaking(boolean shaking) {
        this.entityData.set(IS_SHAKING, shaking);
    }

    public void setLaying(boolean laying) {
        this.entityData.set(IS_LAYING, laying);
    }

    public int getDiggingTicks() {
        return this.entityData.get(DIGGING_TICKS);
    }

    public void setDiggingTicks(int diggingTicks) {
        this.entityData.set(DIGGING_TICKS, diggingTicks);
    }

    public boolean hasEgg() {
        return this.entityData.get(IS_HAS_EGG);
    }

    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(IS_HAS_EGG, hasEgg);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.KELP);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return JustaCreatureMobs.CORARCHELON.get().create(serverLevel);
    }

    int eatTimer = 40;

    @Override
    public void tick() {
        super.tick();
        if (this.isEating() && eatTimer != 0) {
            this.eatAnimationState.startIfStopped(this.tickCount);
            this.eatTimer--;
        } else {
            this.eatAnimationState.stop();
            this.setEating(false);
            this.eatTimer = 40;
        }

        if (this.level().isClientSide()) {
            // Calculate if the mob is currently moving
            double dx = this.getX() - this.xo;
            double dz = this.getZ() - this.zo;
            boolean isMoving = ((dx * dx) + (dz * dz)) > 0.00001D;

            // Ensure it isn't playing a special action
            boolean isDoingAction = this.isEating();

            // If standing perfectly still and not doing an action, play idle!
            if (!isMoving && !isDoingAction) {
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState.stop();
            }
        }
    }

    @Override
    public boolean canDrownInFluidType(net.neoforged.neoforge.fluids.FluidType type) {
        if (type == net.neoforged.neoforge.common.NeoForgeMod.WATER_TYPE.value()) {
            return false; // never drowns in water specifically
        }
        return super.canDrownInFluidType(type);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CorarchelonLayEggGoal(this, 1.0D)); // your goal, replaces TurtleLayEggGoal
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        super.registerGoals();
    }
}
