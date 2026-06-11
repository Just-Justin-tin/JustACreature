package com.example.justacreature;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;

@EventBusSubscriber(modid = JustaCreature.MODID, bus = EventBusSubscriber.Bus.MOD)
public class JustaCreatureMobs {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
                        .create(BuiltInRegistries.ENTITY_TYPE, JustaCreature.MODID);
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM,
                        JustaCreature.MODID);

        public static final DeferredHolder<EntityType<?>, EntityType<CreatureBosambo>> CREATURE_BOSAMBO = ENTITY_TYPES
                        .register("creature_bosambo",
                                        () -> EntityType.Builder.of(CreatureBosambo::new, MobCategory.CREATURE)
                                                        .sized(3.5F, 3.0F)
                                                        .clientTrackingRange(10)
                                                        .fireImmune()
                                                        .build("creature_bosambo"));

        public static final DeferredHolder<EntityType<?>, EntityType<red_Endermaniac>> RED_ENDERMANIAC = ENTITY_TYPES
                        .register("red_endermaniac",
                                        () -> EntityType.Builder.of(red_Endermaniac::new, MobCategory.MONSTER)
                                                        .sized(0.5F, 2.5F)
                                                        .clientTrackingRange(10)
                                                        .fireImmune()
                                                        .build("red_endermaniac"));

        public static final DeferredHolder<EntityType<?>, EntityType<pink_Endermaniac>> PINK_ENDERMANIAC = ENTITY_TYPES
                        .register("pink_endermaniac",
                                        () -> EntityType.Builder.of(pink_Endermaniac::new, MobCategory.MONSTER)
                                                        .sized(0.5F, 1.5F)
                                                        .clientTrackingRange(10)
                                                        .fireImmune()
                                                        .build("pink_endermaniac"));

        public static final DeferredHolder<EntityType<?>, EntityType<blue_Endermaniac>> BLUE_ENDERMANIAC = ENTITY_TYPES
                        .register("blue_endermaniac",
                                        () -> EntityType.Builder.of(blue_Endermaniac::new, MobCategory.MONSTER)
                                                        .sized(0.5F, 2.0F)
                                                        .clientTrackingRange(10)
                                                        .fireImmune()
                                                        .build("blue_endermaniac"));

        public static final DeferredHolder<EntityType<?>, EntityType<EnderManiac>> ENDER_MANIAC = ENTITY_TYPES
                        .register("ender_maniac",
                                        () -> EntityType.Builder.of(EnderManiac::new, MobCategory.MONSTER)
                                                        .sized(0.6F, 2.9F)
                                                        .clientTrackingRange(10)
                                                        .build("ender_maniac"));

        public static final DeferredHolder<EntityType<?>, EntityType<BalloonCreeper>> BALLOON_CREEPER = ENTITY_TYPES
                        .register("balloon_creeper",
                                        () -> EntityType.Builder.of(BalloonCreeper::new, MobCategory.MONSTER)
                                                        .sized(1.0F, 1.0F)
                                                        .clientTrackingRange(10)
                                                        .build("balloon_creeper"));

        public static final DeferredHolder<EntityType<?>, EntityType<BalloonGhast>> BALLOON_GHAST = ENTITY_TYPES
                        .register("balloon_ghast",
                                        () -> EntityType.Builder.of(BalloonGhast::new, MobCategory.MONSTER)
                                                        .sized(4.0F, 4.0F)
                                                        .clientTrackingRange(10)
                                                        .build("balloon_ghast"));

        public static final DeferredHolder<EntityType<?>, EntityType<BalloonWolf>> BALLOON_WOLF = ENTITY_TYPES
                        .register("balloon_wolf",
                                        () -> EntityType.Builder.of(BalloonWolf::new, MobCategory.MONSTER)
                                                        .sized(0.7F, 0.6F)
                                                        .clientTrackingRange(10)
                                                        .build("balloon_wolf"));

        public static final DeferredHolder<EntityType<?>, EntityType<BalloonBee>> BALLOON_BEE = ENTITY_TYPES
                        .register("balloon_bee",
                                        () -> EntityType.Builder.of(BalloonBee::new, MobCategory.MONSTER)
                                                        .sized(0.5F, 0.5F)
                                                        .clientTrackingRange(10)
                                                        .build("balloon_bee"));

        public static final DeferredHolder<EntityType<?>, EntityType<BouncyBall>> BOUNCY_BALL = ENTITY_TYPES
                        .register("bouncyball",
                                        () -> EntityType.Builder.of(BouncyBall::new, MobCategory.MONSTER)
                                                        .sized(0.5F, 0.5F)
                                                        .clientTrackingRange(10)
                                                        .build("bouncyball"));

        public static final DeferredHolder<EntityType<?>, EntityType<PinProjectile>> PIN_PROJECTILE = ENTITY_TYPES
                        .register("pin_projectile",
                                        () -> EntityType.Builder
                                                        .<PinProjectile>of(
                                                                        (type, level) -> new PinProjectile(type, level),
                                                                        MobCategory.MISC)
                                                        .sized(0.4F, 0.4F)
                                                        .clientTrackingRange(4)
                                                        .updateInterval(10)
                                                        .build("pin_projectile"));

        public static final DeferredHolder<EntityType<?>, EntityType<EnderManiac>> ENDERMANIAC = ENTITY_TYPES
                        .register("endermanic",
                                        () -> EntityType.Builder.of(EnderManiac::new, MobCategory.MONSTER)
                                                        .sized(0.6F, 1.8F)
                                                        .clientTrackingRange(40)
                                                        .build("endermanic"));

        public static final DeferredHolder<Item, Item> BOSAMBO_SPAWN_EGG = ITEMS.register("creature_bosambo_spawn_egg",
                        () -> new DeferredSpawnEggItem(CREATURE_BOSAMBO, 0x8A3A35, 0x219C4A, new Item.Properties()));

        @SubscribeEvent
        public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
                event.put(CREATURE_BOSAMBO.get(), Sniffer.createAttributes()
                                .add(Attributes.MAX_HEALTH, 300.0D)
                                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                                .add(Attributes.ARMOR, 30.0D)
                                .add(Attributes.ARMOR_TOUGHNESS, 20.0D)
                                .add(Attributes.STEP_HEIGHT, 1.0D)
                                .build());

                event.put(RED_ENDERMANIAC.get(), EnderMan.createAttributes()
                                .add(Attributes.MAX_HEALTH, 40.0D)
                                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                                .add(Attributes.FOLLOW_RANGE, 32.0D)
                                .build());

                event.put(PINK_ENDERMANIAC.get(), EnderMan.createAttributes()
                                .add(Attributes.MAX_HEALTH, 40.0D)
                                .add(Attributes.ATTACK_DAMAGE, 7.5D)
                                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                                .add(Attributes.FOLLOW_RANGE, 32.0D)
                                .build());

                event.put(BLUE_ENDERMANIAC.get(), Monster.createMonsterAttributes()
                                .add(Attributes.MAX_HEALTH, 40.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                                .add(Attributes.FLYING_SPEED, 1.0D)
                                .add(Attributes.FOLLOW_RANGE, 32.0D)
                                .add(Attributes.GRAVITY, 0.05D)
                                .build());

                event.put(ENDER_MANIAC.get(), EnderMan.createAttributes()
                                .add(Attributes.MAX_HEALTH, 60.0D)
                                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                                .add(Attributes.FOLLOW_RANGE, 32.0D)
                                .build());

                event.put(BALLOON_WOLF.get(), Wolf.createAttributes()
                                .add(Attributes.MAX_HEALTH, 1.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                                .build());

                event.put(BALLOON_CREEPER.get(), Creeper.createAttributes()
                                .add(Attributes.MAX_HEALTH, 1.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                                .build());

                event.put(BALLOON_GHAST.get(), Ghast.createAttributes()
                                .add(Attributes.MAX_HEALTH, 1.0D)
                                .build());

                event.put(BALLOON_BEE.get(), Bee.createAttributes()
                                .add(Attributes.MAX_HEALTH, 1.0D)
                                .add(Attributes.FLYING_SPEED, 1.0D)
                                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                                .build());

                event.put(BOUNCY_BALL.get(), Monster.createMonsterAttributes()
                                .build());

                event.put(ENDERMANIAC.get(), Monster.createMonsterAttributes()
                                .build());
        }

        @EventBusSubscriber(modid = JustaCreature.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientModEvents {
                @SubscribeEvent
                public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
                        event.registerEntityRenderer(CREATURE_BOSAMBO.get(),
                                        com.example.justacreature.client.renderer.BosamboRenderer::new);

                        event.registerEntityRenderer(RED_ENDERMANIAC.get(),
                                        com.example.justacreature.client.renderer.red_EndermaniacRenderer::new);

                        event.registerEntityRenderer(PINK_ENDERMANIAC.get(),
                                        com.example.justacreature.client.renderer.pink_EndermaniacRenderer::new);

                        event.registerEntityRenderer(BLUE_ENDERMANIAC.get(),
                                        com.example.justacreature.client.renderer.blue_EndermaniacRenderer::new);

                        event.registerEntityRenderer(ENDER_MANIAC.get(),
                                        com.example.justacreature.client.renderer.EnderManiacRenderer::new);

                        event.registerEntityRenderer(PIN_PROJECTILE.get(),
                                        com.example.justacreature.client.renderer.PinProjectileRenderer::new);

                        event.registerEntityRenderer(BALLOON_WOLF.get(),
                                        com.example.justacreature.client.renderer.BalloonWolfRenderer::new);

                        event.registerEntityRenderer(BALLOON_GHAST.get(),
                                        com.example.justacreature.client.renderer.BalloonGhastRenderer::new);

                        event.registerEntityRenderer(BALLOON_CREEPER.get(),
                                        com.example.justacreature.client.renderer.BalloonCreeperRenderer::new);

                        event.registerEntityRenderer(BALLOON_BEE.get(),
                                        com.example.justacreature.client.renderer.BalloonBeeRenderer::new);

                        event.registerEntityRenderer(BOUNCY_BALL.get(),
                                        com.example.justacreature.client.renderer.BouncyBallRenderer::new);
                }

                @SubscribeEvent
                public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.BosamboModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.BosamboModel::createBodyLayer);

                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.red_EndermaniacModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.red_EndermaniacModel::createBodyLayer);

                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.pink_EndermaniacModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.pink_EndermaniacModel::createBodyLayer);

                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.blue_EndermaniacModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.blue_EndermaniacModel::createBodyLayer);

                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.EnderManiacModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.EnderManiacModel::createBodyLayer);

                        event.registerLayerDefinition(
                                        com.example.justacreature.client.model.PinProjectileModel.LAYER_LOCATION,
                                        com.example.justacreature.client.model.PinProjectileModel::createBodyLayer);
                }
        }
}
