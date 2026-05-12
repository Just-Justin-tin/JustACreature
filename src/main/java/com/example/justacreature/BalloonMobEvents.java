package com.example.justacreature;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber(modid = JustaCreature.MODID)
public class BalloonMobEvents {

    @SubscribeEvent
    public static void onBalloonDeath(LivingDeathEvent event) {
        // Check if the dying mob has our special balloon tag
        if (event.getEntity().getTags().contains("is_balloon_mob")) {

            // Play a popping sound (Chicken egg breaking sounds like a pop!)
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), SoundEvents.CHICKEN_EGG,
                    SoundSource.HOSTILE, 1.0F, 0.5F);

            // Spawn a bunch of colored particles to look like a balloon bursting
            if (event.getEntity().level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.NOTE,
                        event.getEntity().getX(), event.getEntity().getY() + 1.0D, event.getEntity().getZ(),
                        10, 0.4D, 0.4D, 0.4D, 0.5D);
            }

            // Instantly remove the entity so it doesn't play the slow "falling over" death
            // animation
            event.getEntity().discard();
        }
    }

    @SubscribeEvent
    public static void onFireballSpawn(EntityJoinLevelEvent event) {
        // Check if the entity spawning into the world is a Fireball
        if (event.getEntity() instanceof LargeFireball fireball) {
            // Check if the shooter was our Custom Balloon Ghast
            if (fireball.getOwner() instanceof BalloonGhast ghast) {
                if (ghast.nextShotIsWindCharge == false) {
                    // Write the fireball's data, force ExplosionPower to 0, and load it back!
                    CompoundTag tag = new CompoundTag();
                    fireball.saveWithoutId(tag);
                    tag.putByte("ExplosionPower", (byte) 0);
                    fireball.load(tag);

                    ghast.nextShotIsWindCharge = true;
                } else {
                    // Write the fireball's data, force ExplosionPower to 0, and load it back!
                    WindCharge windCharge = EntityType.WIND_CHARGE.create(fireball.level());
                    if (windCharge != null) {

                        // Match the exact position of where the fireball was going to spawn
                        windCharge.moveTo(fireball.getX(), fireball.getY(), fireball.getZ());

                        // Transfer the speed and direction so it shoots straight at the player
                        windCharge.setDeltaMovement(fireball.getDeltaMovement().scale(5.0D));

                        // Tell the game the Balloon Ghast fired it
                        windCharge.setOwner(fireball.getOwner());

                        // Add the wind charge to the world!
                        fireball.level().addFreshEntity(windCharge);

                        ghast.nextShotIsWindCharge = false;
                    }

                    // 4. CANCEL the fireball spawn so the fireball never actually appears
                    event.setCanceled(true);
                }

                // Now the fireball will just hit the player and disappear without breaking
                // blocks!
            }
        }

    }

    @SubscribeEvent
    public static void onCreeperExplosion(ExplosionEvent.Detonate event) {
        // Find out who caused this explosion
        Entity exploder = event.getExplosion().getDirectSourceEntity();

        // Is it our Balloon Creeper?
        if (exploder instanceof BalloonCreeper) {

            // 1. Clear all affected blocks. This leaves the entity damage perfectly intact
            // but completely stops the explosion from breaking terrain!
            event.getAffectedBlocks().clear();

            // 2. Play Firework sounds instead of the normal TNT boom
            exploder.level().playSound(null, exploder.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST,
                    SoundSource.HOSTILE, 2.0F, 1.0F);
            exploder.level().playSound(null, exploder.blockPosition(), SoundEvents.FIREWORK_ROCKET_TWINKLE,
                    SoundSource.HOSTILE, 2.0F, 1.0F);

            // 3. Spawn a massive burst of firework particles!
            if (exploder.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FIREWORK,
                        exploder.getX(), exploder.getY() + 1.0D, exploder.getZ(),
                        100, 0.5D, 0.5D, 0.5D, 0.2D);
            }
        }
    }
}