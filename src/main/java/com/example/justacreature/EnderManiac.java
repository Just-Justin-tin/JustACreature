package com.example.justacreature;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class EnderManiac extends Monster {

    private static final EntityDataAccessor<Boolean> IS_REVEALING = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_DISGUISED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_REVEALED = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_TRADING = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CAN_LOOK_AT_PLAYER = SynchedEntityData
            .defineId(EnderManiac.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState revealAnimationState = new AnimationState();

    private BlockPos balloonPos;

    private int AnimationTimer = -1;

    private int TradeTimer = -1;

    private boolean forcedPersistent = true;

    private int pendingChorusCount;

    private Player tradedPlayer;

    public void setForcedPersistent(boolean value) {
        this.forcedPersistent = value;
    }

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

    public void setTrading(boolean trading) {
        this.entityData.set(IS_TRADING, trading);
    }

    public void setCanLookAtPlayer(boolean canLookAtPlayer) {
        this.entityData.set(CAN_LOOK_AT_PLAYER, canLookAtPlayer);
    }

    public boolean canLookAtPlayer() {
        return this.entityData.get(CAN_LOOK_AT_PLAYER);
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

    public boolean isTrading() {
        return this.entityData.get(IS_TRADING);
    }

    public EnderManiac(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    private static Block[] TRADE_BALLOON_ITEMS = null;

    private static Block[] getTradeBalloonItems() {
        if (TRADE_BALLOON_ITEMS == null) {
            TRADE_BALLOON_ITEMS = new Block[] {
                    JustaCreature.RED_BALLOON.get(),
                    JustaCreature.WHITE_BALLOON.get(),
                    JustaCreature.BLUE_BALLOON.get(),
                    JustaCreature.GREEN_BALLOON.get(),
                    JustaCreature.YELLOW_BALLOON.get(),
                    JustaCreature.MAGENTA_BALLOON.get(),
                    JustaCreature.ORANGE_BALLOON.get(),
                    JustaCreature.PINK_BALLOON.get(),
                    JustaCreature.CYAN_BALLOON.get(),
                    JustaCreature.PURPLE_BALLOON.get(),
                    JustaCreature.GRAY_BALLOON.get(),
                    JustaCreature.LIGHT_BLUE_BALLOON.get(),
                    JustaCreature.LIME_BALLOON.get(),
                    JustaCreature.BROWN_BALLOON.get(),
                    JustaCreature.BLACK_BALLOON.get(),
                    JustaCreature.LIGHT_GRAY_BALLOON.get(),
            };
        }
        return TRADE_BALLOON_ITEMS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_REVEALING, false);
        builder.define(IS_DISGUISED, true);
        builder.define(HAS_REVEALED, false);
        builder.define(IS_TRADING, false);
        builder.define(CAN_LOOK_AT_PLAYER, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // If the mob takes damage on the server, cancel the animation
        if (!this.level().isClientSide() && this.entityData.get(IS_DISGUISED)) {
            this.setRevealing(true);
            this.setRevealed(false);
            this.setDisguised(false);
            this.setTrading(false);
            this.setCanLookAtPlayer(false);
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

    // look at balloon method
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.balloonPos != null) {
            tag.putInt("BalloonX", this.balloonPos.getX());
            tag.putInt("BalloonY", this.balloonPos.getY());
            tag.putInt("BalloonZ", this.balloonPos.getZ());
        }
        tag.putBoolean("IsRevealing", this.entityData.get(IS_REVEALING));
        tag.putBoolean("IsDisguised", this.entityData.get(IS_DISGUISED));
        tag.putBoolean("HasRevealed", this.entityData.get(HAS_REVEALED));
        tag.putBoolean("IsTrading", this.entityData.get(IS_TRADING));
        tag.putBoolean("ForcedPersistent", this.forcedPersistent);
    }

    // look at balloon method
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("BalloonX")) {
            this.balloonPos = new BlockPos(tag.getInt("BalloonX"), tag.getInt("BalloonY"), tag.getInt("BalloonZ"));
        }
        this.entityData.set(IS_REVEALING, tag.getBoolean("IsRevealing"));
        this.entityData.set(IS_DISGUISED, tag.getBoolean("IsDisguised"));
        this.entityData.set(HAS_REVEALED, tag.getBoolean("HasRevealed"));
        this.entityData.set(IS_TRADING, tag.getBoolean("IsTrading"));
        this.forcedPersistent = tag.getBoolean("ForcedPersistent");
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
    public boolean isPersistenceRequired() {
        return this.forcedPersistent;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var heldStack = player.getItemInHand(hand);

        // Lost balloon to trade method
        if (heldStack.is(JustaCreature.LOST_BALLOON_ITEM.get()) && !this.isTrading()) {
            if (!this.level().isClientSide()) {
                this.setTrading(true);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                this.playSound(net.minecraft.sounds.SoundEvents.ENDERMAN_AMBIENT, 1.0F, 1.0F);

            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        // Chorus fruit to balloon trading method
        else if (heldStack.is(Items.CHORUS_FRUIT) && this.isTrading() && this.TradeTimer <= 0) {
            if (!this.level().isClientSide()) {
                int chorusAmmount = heldStack.getCount();
                if (!player.isCreative()) {
                    heldStack.shrink(chorusAmmount);
                }
                this.tradedPlayer = player;
                this.TradeTimer = random.nextInt(60) + 60;

                this.playSound(net.minecraft.sounds.SoundEvents.ENDERMAN_AMBIENT, 1.0F, 1.0F);
                this.pendingChorusCount = chorusAmmount;
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 lookDirection = this.getLookAngle();

        if (this.TradeTimer > 0) {
            this.TradeTimer--;
        }

        if (this.TradeTimer == 0) {
            for (int i = 0; i < this.pendingChorusCount; i++) {
                ItemStack stack = new ItemStack(
                        getTradeBalloonItems()[random.nextInt(getTradeBalloonItems().length)].asItem(),
                        4);
                net.minecraft.world.entity.item.ItemEntity item = new net.minecraft.world.entity.item.ItemEntity(
                        this.level(), getX(), getY() + 1.5, getZ(), stack);

                item.setDeltaMovement(lookDirection.x + this.random.nextFloat() * 0.05, 0.0,
                        lookDirection.z + this.random.nextFloat() * 0.05);
                this.level().addFreshEntity(item);
            }
            this.TradeTimer = -1;
        }

        if (this.balloonPos != null && !this.isTrading()) {
            this.getLookControl().setLookAt(
                    this.balloonPos.getX() + 0.5,
                    this.balloonPos.getY() + 0.5,
                    this.balloonPos.getZ() + 0.5,
                    30.0F, 30.0F);
        } else if (this.isTrading() && this.TradeTimer > 0 || this.isRevealing()) {
            if (this.tradedPlayer != null) {
                this.getLookControl().setLookAt(
                        this.tradedPlayer.getX(),
                        this.tradedPlayer.getEyeY(),
                        this.tradedPlayer.getZ(),
                        30.0F, 30.0F);
            }
        }

        if (this.isRevealed() && this.AnimationTimer > 0) {
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ConditionalLookAtPlayerGoal(this, Player.class, 8.0F));
        return;
    }
}
