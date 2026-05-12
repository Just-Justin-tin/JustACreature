package com.example.justacreature;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.damagesource.DamageSource;
import java.util.ArrayList;
import java.util.List;

public class CreatureBosambo extends Sniffer {

    // 1. The ID for syncing across the network
    private static final EntityDataAccessor<Boolean> IS_DIGGING_BOSAMBO = SynchedEntityData
            .defineId(CreatureBosambo.class, EntityDataSerializers.BOOLEAN);

    public int digAnimationTick = 0;
    public int walkSoundTick = 0;
    public String melonType;

    // 2. The custom animation state (Must match the Model exactly!)
    public final AnimationState digAnimationState = new AnimationState();

    private static final Set<Block> BOSAMBO_DIGGABLE_BLOCK = Set.of(
            Blocks.DEEPSLATE, Blocks.OBSIDIAN, Blocks.ANDESITE,
            Blocks.DIORITE, Blocks.GRANITE, Blocks.TUFF);

    public CreatureBosambo(EntityType<? extends Sniffer> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // 1. New Entity Data Accessors
    private static final EntityDataAccessor<Boolean> IS_LAYING = SynchedEntityData
            .defineId(CreatureBosambo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData
            .defineId(CreatureBosambo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_STANDING = SynchedEntityData
            .defineId(CreatureBosambo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SADDLED = SynchedEntityData
            .defineId(CreatureBosambo.class, EntityDataSerializers.BOOLEAN);

    // 2. New Animation States
    public final AnimationState layAnimationState = new AnimationState();
    public final AnimationState sleepAnimationState = new AnimationState();
    public final AnimationState standAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();

    public void setLaying(boolean laying) {
        this.entityData.set(IS_LAYING, laying);
    }

    public boolean isLaying() {
        return this.entityData.get(IS_LAYING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(IS_SLEEPING, sleeping);
    }

    public void setStanding(boolean standing) {
        this.entityData.set(IS_STANDING, standing);
    }

    public boolean isSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    public boolean isStanding() {
        return this.entityData.get(IS_STANDING);
    }

    public boolean isDigging() {
        return this.entityData.get(IS_DIGGING_BOSAMBO);
    }

    public boolean isSaddled() {
        return this.entityData.get(IS_SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(IS_SADDLED, saddled);
    }

    private static final Block[] POSSIBLE_ORES = new Block[] {
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.COBBLESTONE
    };

    private static final Block[] PREMIUM_POSSIBLE_ORES = new Block[] {
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK,
            Blocks.GOLD_BLOCK,
            Blocks.IRON_BLOCK,
            Blocks.COAL_BLOCK,
            Blocks.LAPIS_BLOCK,
            Blocks.REDSTONE_BLOCK,
            Blocks.IRON_BLOCK,
            JustaCreature.DEEPSLATE_TUNGSTEN_ORE.get(),
            JustaCreature.DEEPSLATE_TUNGSTEN_ORE.get()

    };

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DIGGING_BOSAMBO, false);
        builder.define(IS_LAYING, false);
        builder.define(IS_SLEEPING, false);
        builder.define(IS_STANDING, false);
        builder.define(IS_SADDLED, false);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var heldStack = player.getItemInHand(hand);

        if (heldStack.is(JustaCreature.BOSAMBO_SADDLE.get()) && !this.isSaddled()) {
            if (!this.level().isClientSide()) {
                this.setSaddled(true);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                this.playSound(net.minecraft.sounds.SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
                this.playSound(net.minecraft.sounds.SoundEvents.ANVIL_LAND, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (heldStack.is(Items.AIR) && player.isSecondaryUseActive() && this.isSaddled()) {
            if (!this.level().isClientSide()) {
                this.setSaddled(false);
                player.getInventory().add(new ItemStack(JustaCreature.BOSAMBO_SADDLE.get()));
            }
            this.playSound(net.minecraft.sounds.SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        // 2. Mount the Mob (If saddled and player is empty-handed/not shifting)
        if (this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide()) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (heldStack.is(Items.GLISTERING_MELON_SLICE) && this.entityData.get(IS_DIGGING_BOSAMBO) == false) {
            // 1. Get the block directly beneath Bosambo's feet
            Block blockBelow = this.level().getBlockState(this.blockPosition().below()).getBlock();
            melonType = "Glistering";

            // 2. Check if the block is in your custom list
            if (BOSAMBO_DIGGABLE_BLOCK.contains(blockBelow)) {

                // Only trigger if he is standing on a valid block
                if (!this.level().isClientSide()) {
                    this.entityData.set(IS_DIGGING_BOSAMBO, true);
                    this.digAnimationTick = 0; // Start the timer

                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }

        if (heldStack.is(JustaCreature.CRYSTALLINE_MELON_SLICE.get())
                && this.entityData.get(IS_DIGGING_BOSAMBO) == false) {
            // 1. Get the block directly beneath Bosambo's feet
            Block blockUnder = this.level().getBlockState(this.blockPosition().below()).getBlock();
            melonType = "Crystalline";

            // 2. Check if the block is in your custom list
            if (BOSAMBO_DIGGABLE_BLOCK.contains(blockUnder)) {

                // Only trigger if he is standing on a valid block
                if (!this.level().isClientSide()) {
                    this.entityData.set(IS_DIGGING_BOSAMBO, true);
                    this.digAnimationTick = 0; // Start the timer

                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
                // If he is NOT standing on a valid block, reject the interaction.
                // The melon won't be consumed and the animation won't play.
                return InteractionResult.PASS;
            }
        }

        // Fall back to the default interaction for all other items
        return super.mobInteract(player, hand);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (IS_DIGGING_BOSAMBO.equals(key)) {
            if (this.level().isClientSide()) {
                if (this.entityData.get(IS_DIGGING_BOSAMBO)) {
                    this.digAnimationState.start(this.tickCount);
                } else {
                    this.digAnimationState.stop();
                }
            }
        }
        // ADD THIS:
        if (IS_LAYING.equals(key)) {
            if (this.level().isClientSide()) {
                if (this.entityData.get(IS_LAYING)) {
                    this.layAnimationState.start(this.tickCount);
                } else {
                    this.layAnimationState.stop();
                }
            }
        }

        if (IS_SLEEPING.equals(key)) {
            if (this.level().isClientSide()) {
                if (this.entityData.get(IS_SLEEPING)) {
                    this.sleepAnimationState.start(this.tickCount);
                } else {
                    this.sleepAnimationState.stop();
                }
            }
        }

        if (IS_STANDING.equals(key)) {
            if (this.level().isClientSide()) {
                if (this.entityData.get(IS_STANDING)) {
                    this.standAnimationState.start(this.tickCount);
                } else {
                    this.standAnimationState.stop();
                }
            }
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void tick() {
        super.tick();

        // 0. FREEZE ROTATION: Run on BOTH client and server so the visual model doesn't
        // twitch
        if (this.entityData.get(IS_DIGGING_BOSAMBO)) {
            this.setYRot(this.yRotO);
            this.setXRot(this.xRotO);
            this.yBodyRot = this.yBodyRotO;
            this.yHeadRot = this.yHeadRotO;
        }

        // --- DIGGING LOGIC ---
        if (!this.level().isClientSide() && this.entityData.get(IS_DIGGING_BOSAMBO)) {
            this.digAnimationTick++;

            if (this.digAnimationTick == 35) {
                this.playSound(com.example.justacreature.JustaCreature.BOSAMBO_DIG_STOP.get(), 1.0F,
                        0.9F + this.random.nextFloat() * 0.2F);
            }

            if (this.digAnimationTick == 50) {
                Vec3 lookDirection = this.getLookAngle();
                double spawnX = this.getX() + (lookDirection.x * 4.5);
                double spawnY = this.getY() + 0.5;
                double spawnZ = this.getZ() + (lookDirection.z * 4.5);
                Block randomOre = null;

                if (melonType == "Crystalline") {
                    int randomIndex = this.random.nextInt(PREMIUM_POSSIBLE_ORES.length);
                    randomOre = PREMIUM_POSSIBLE_ORES[randomIndex];
                } else if (melonType == "Glistering") {
                    int randomIndex = this.random.nextInt(POSSIBLE_ORES.length);
                    randomOre = POSSIBLE_ORES[randomIndex];
                }

                if (!this.level().isClientSide() && randomOre != Blocks.COBBLESTONE) {
                    for (int orespawnnum = this.random.nextInt(1, 4); orespawnnum <= 3; orespawnnum++) {
                        if (melonType == "Crystalline") {
                            do {
                                int randomIndex = this.random.nextInt(PREMIUM_POSSIBLE_ORES.length);
                                randomOre = PREMIUM_POSSIBLE_ORES[randomIndex];
                            } while (randomOre == Blocks.COBBLESTONE);
                        } else if (melonType == "Glistering") {
                            int randomIndex = this.random.nextInt(POSSIBLE_ORES.length - 1);
                            randomOre = POSSIBLE_ORES[randomIndex];
                        }

                        net.minecraft.world.entity.item.FallingBlockEntity fallingBlock = new net.minecraft.world.entity.item.FallingBlockEntity(
                                net.minecraft.world.entity.EntityType.FALLING_BLOCK, this.level());
                        fallingBlock.setPos(spawnX, spawnY, spawnZ);
                        net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
                        fallingBlock.saveWithoutId(nbt);
                        net.minecraft.world.level.block.state.BlockState blockState = randomOre.defaultBlockState();
                        nbt.put("BlockState", net.minecraft.nbt.NbtUtils.writeBlockState(blockState));
                        nbt.putInt("Time", 1);
                        fallingBlock.load(nbt);
                        fallingBlock.setDeltaMovement(lookDirection.x + this.random.nextFloat() * 0.5, 0.75,
                                lookDirection.z + this.random.nextFloat() * 0.5);
                        this.level().addFreshEntity(fallingBlock);
                    }

                    if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        for (int i = 0; i < 4; i++) {
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, spawnX, spawnY,
                                    spawnZ, 1, 0.2, 0.2, 0.2, 0.0);
                        }
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.GUST, spawnX, spawnY,
                                spawnZ, 1, 0.0, 0.0, 0.0, 0.0);
                        serverLevel.playSound(null, net.minecraft.core.BlockPos.containing(spawnX, spawnY, spawnZ),
                                net.minecraft.sounds.SoundEvents.DEEPSLATE_BREAK,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 0.4F + this.random.nextFloat() * 0.1F);
                        serverLevel.playSound(null, net.minecraft.core.BlockPos.containing(spawnX, spawnY, spawnZ),
                                net.minecraft.sounds.SoundEvents.POINTED_DRIPSTONE_LAND,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 0.4F + this.random.nextFloat() * 0.1F);
                    }
                }

                if (!this.level().isClientSide() && randomOre == Blocks.COBBLESTONE) {
                    net.minecraft.world.entity.item.ItemEntity item = new net.minecraft.world.entity.item.ItemEntity(
                            this.level(), 0, 0, 0, JustaCreature.SHINY_SEEDS.get().getDefaultInstance());
                    item.setPos(spawnX, spawnY, spawnZ);
                    net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
                    item.saveWithoutId(nbt);
                    item.load(nbt);
                    item.setDeltaMovement(lookDirection.x + this.random.nextFloat() * 0.5, 0.75,
                            lookDirection.z + this.random.nextFloat() * 0.5);
                    this.level().addFreshEntity(item);

                    if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        for (int i = 0; i < 4; i++) {
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, spawnX, spawnY,
                                    spawnZ, 1, 0.2, 0.2, 0.2, 0.0);
                        }
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.GUST, spawnX, spawnY,
                                spawnZ, 1, 0.0, 0.0, 0.0, 0.0);
                        serverLevel.playSound(null, net.minecraft.core.BlockPos.containing(spawnX, spawnY, spawnZ),
                                net.minecraft.sounds.SoundEvents.DEEPSLATE_STEP,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 0.4F + this.random.nextFloat() * 0.1F);
                        serverLevel.playSound(null, net.minecraft.core.BlockPos.containing(spawnX, spawnY, spawnZ),
                                net.minecraft.sounds.SoundEvents.POINTED_DRIPSTONE_LAND,
                                net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 0.4F + this.random.nextFloat() * 0.1F);
                    }
                }
            }
        }

        // Reset the digging animation properly
        if (this.digAnimationTick >= 100) {
            this.entityData.set(IS_DIGGING_BOSAMBO, false);
            this.digAnimationTick = 0;
        }

        // --- TIME-BASED FOOTSTEP LOGIC ---
        if (!this.level().isClientSide() && !this.entityData.get(IS_DIGGING_BOSAMBO)) {
            double dx = this.getX() - this.xo;
            double dz = this.getZ() - this.zo;
            double horizontalMovement = (dx * dx) + (dz * dz);

            if (horizontalMovement > 0.00001D) {
                this.walkSoundTick++;
                if (this.walkSoundTick >= 30) {
                    this.playSound(com.example.justacreature.JustaCreature.BOSAMBO_STEP.get(), 1.0F, 1.0F);
                    this.walkSoundTick = 0;
                }
            } else {
                this.walkSoundTick = 0;
            }
        }

        if (this.level().isClientSide()) {
            // Calculate if the mob is currently moving
            double dx = this.getX() - this.xo;
            double dz = this.getZ() - this.zo;
            boolean isMoving = ((dx * dx) + (dz * dz)) > 0.00001D;

            // Ensure it isn't playing a special action
            boolean isDoingAction = this.isDigging() || this.isLaying() || this.isSleeping() || this.isStanding();

            // If standing perfectly still and not doing an action, play idle!
            if (!isMoving && !isDoingAction) {
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState.stop();
            }
        }

        // --- SLEEPING SNORE PARTICLES ---
        if (this.isSleeping()) {
            Vec3 sleepDirection = this.getLookAngle();
            if (this.tickCount % 50 == 0) {
                // Fixed the lowercase "serverLevel" declaration here:
                if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    double rightNoseX = this.getX() + (sleepDirection.x * 4.75) + (sleepDirection.z * 0.25);
                    double rightNoseY = this.getY() + 0.8D;
                    double rightNoseZ = this.getZ() + (sleepDirection.z * 4.75) - (sleepDirection.x * 0.25);
                    double leftNoseX = this.getX() + (sleepDirection.x * 4.75) - (sleepDirection.z * 0.25);
                    double leftNoseY = this.getY() + 0.8D;
                    double leftNoseZ = this.getZ() + (sleepDirection.z * 4.75) + (sleepDirection.x * 0.25);

                    // 3. Spawn a lazy, drifting campfire smoke particle
                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            rightNoseX, rightNoseY, rightNoseZ,
                            0, // THE MAGIC 0: This tells Minecraft "Do not randomize the direction!"
                            0.0D, 0.05D, 0.0D, // X, Y, and Z Velocity: (0 for sides, 0.05 for UP)
                            1.0D // Speed Multiplier (Keep this at 1.0 when using the Magic 0)
                    );

                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            leftNoseX, leftNoseY, leftNoseZ,
                            0, // THE MAGIC 0: This tells Minecraft "Do not randomize the direction!"
                            0.0D, 0.05D, 0.0D, // X, Y, and Z Velocity: (0 for sides, 0.05 for UP)
                            1.0D // Speed Multiplier (Keep this at 1.0 when using the Magic 0)
                    );
                }
            }
        }
    }

    // 1. Tells the game the player steering it is the "driver"
    @Override
    public net.minecraft.world.entity.LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
    }

    // 2. Put the player on his back, not in his stomach!
    @Override
    protected void positionRider(net.minecraft.world.entity.Entity passenger,
            net.minecraft.world.entity.Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger)) {
            // Adjust the -0.2D up or down to make the player sit perfectly on the model
            double yOffset = this.getBbHeight() - 0.2D;
            callback.accept(passenger, this.getX(), this.getY() + yOffset, this.getZ());
        }
    }

    // 3. Sync his rotation to the player's camera while riding
    @Override
    protected void tickRidden(Player passenger, Vec3 travelVector) {
        super.tickRidden(passenger, travelVector);
        this.setRot(passenger.getYRot(), passenger.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    // 2. Translates WASD into movement vectors
    @Override
    protected Vec3 getRiddenInput(Player passenger, Vec3 travelVector) {
        float xRidden = passenger.xxa * 0.5F; // A and D keys
        float zRidden = passenger.zza; // W and S keys

        if (zRidden <= 0.0F) {
            zRidden *= 0.25F; // Walk backwards slowly
        }
        return new Vec3(xRidden, 0.0D, zRidden);
    }

    @Override
    public void travel(Vec3 travelVector) {
        // Check our Synced Data to see if the animation is currently active
        if (this.isDigging() || this.isLaying() || this.isSleeping() || this.isStanding()) {
            if (this.getNavigation().isInProgress()) {
                this.getNavigation().stop();
            }
            // Zero out X and Z, keep Y so gravity works
            super.travel(new Vec3(0, travelVector.y, 0));
            return;
        }

        // If he is NOT, let him move around normally
        super.travel(travelVector);
    }

    // 4. THIS IS THE CRITICAL FIX: Tells the mob how fast to go while ridden!
    @Override
    protected float getRiddenSpeed(Player passenger) {
        // Gets Bosambo's base speed and multiplies it by 0.8 so he isn't insanely fast
        return (float) this.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)
                * 0.3F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // If the mob takes damage on the server, cancel the animation
        if (!this.level().isClientSide() && this.entityData.get(IS_DIGGING_BOSAMBO)) {
            this.entityData.set(IS_DIGGING_BOSAMBO, false);
            this.digAnimationTick = 0; // Reset our timer
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushable() {
        // If it's laying or sleeping, it cannot be pushed!
        if (this.isLaying() || this.isSleeping() || this.isStanding()) {
            return false;
        }
        return super.isPushable();
    }

    @Override
    public net.minecraft.world.entity.AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level,
            net.minecraft.world.entity.AgeableMob otherParent) {
        // When bred (or when a spawn egg is used on an adult), spawn a new Bosambo!
        return JustaCreatureMobs.CREATURE_BOSAMBO.get().create(level);
    }

    // --- CUSTOM SOUND OVERRIDES ---

    @Override
    protected net.minecraft.sounds.SoundEvent getAmbientSound() {
        // The sound the mob makes randomly while walking around
        return com.example.justacreature.JustaCreature.BOSAMBO_AMBIENT.get();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getHurtSound(DamageSource damageSource) {
        // The sound when you punch it
        return com.example.justacreature.JustaCreature.BOSAMBO_HURT.get();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getDeathSound() {
        // The sound when it dies
        return com.example.justacreature.JustaCreature.BOSAMBO_DEATH.get();
    }

    // --- CUSTOM FOOTSTEP SOUND ---
    @Override
    protected void playStepSound(net.minecraft.core.BlockPos pos,
            net.minecraft.world.level.block.state.BlockState block) {
    }
    // --- MUTE VANILLA SNIFFER AI SOUNDS ---

    @Override
    public void playSound(net.minecraft.sounds.SoundEvent sound, float volume, float pitch) {
        // Check if the sound the brain is trying to play is one of the hardcoded
        // Sniffer noises
        if (sound == net.minecraft.sounds.SoundEvents.SNIFFER_SNIFFING ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_SCENTING ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_SEARCHING ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_DIGGING ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_DIGGING_STOP ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_HAPPY ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_STEP ||
                sound == net.minecraft.sounds.SoundEvents.SNIFFER_DROP_SEED) {

            // Return immediately without calling super, effectively muting the sound!
            return;
        }

        // Let all other sounds (like your custom ones) play normally
        super.playSound(sound, volume, pitch);
    }

    // --- LOWER THE AMBIENT SOUND VOLUME ---
    @Override
    protected float getSoundVolume() {
        return 0.4F; // Makes hurt, death, and ambient sounds quieter
    }

    @Override
    protected void registerGoals() {
        super.registerGoals(); // Keep default Sniffer behaviors
        // Add our custom goal! '0' is the highest priority, so it will override
        // wandering.
        this.goalSelector.addGoal(0, new SleepNearLavaGoal(this));
    }

    @Override
    public boolean isInLava() {
        return false; // Lie to the game so he doesn't get slowed down by lava!
    }

    @Override
    public boolean isPushedByFluid() {
        return false; // Too heavy to be pushed by currents!
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsSaddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSaddled(compound.getBoolean("IsSaddled"));
    }

}