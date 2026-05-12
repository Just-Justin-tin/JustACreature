package com.example.justacreature;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

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
}