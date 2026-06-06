package com.example.justacreature;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.SoundType;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JustaCreature.MODID)
public class JustaCreature {
        // Define mod id in a common place for everything to reference
        public static final String MODID = "justacreature";
        // Directly reference a slf4j logger
        public static final Logger LOGGER = LogUtils.getLogger();
        // Create a Deferred Register to hold Blocks which will all be registered under
        // the "justacreature" namespace
        public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
        // Create a Deferred Register to hold Items which will all be registered under
        // the "justacreature" namespace
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
        // Create a Deferred Register to hold CreativeModeTabs which will all be
        // registered under the "justacreature" namespace
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, MODID);

        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT,
                        MODID);

        public static final DeferredBlock<Block> DEEPSLATE_TUNGSTEN_ORE = BLOCKS
                        .registerSimpleBlock("deepslate_tungsten_ore",
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)
                                                        .requiresCorrectToolForDrops());

        public static final DeferredItem<BlockItem> DEEPSLATE_TUNGSTEN_ORE_ITEM = ITEMS
                        .registerSimpleBlockItem("deepslate_tungsten_ore", DEEPSLATE_TUNGSTEN_ORE);

        public static final DeferredBlock<Block> BLOCK_OF_TUNGSTEN = BLOCKS
                        .registerSimpleBlock("block_of_tungsten",
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)
                                                        .requiresCorrectToolForDrops());

        public static final DeferredItem<BlockItem> BLOCK_OF_TUNGSTEN_ITEM = ITEMS
                        .registerSimpleBlockItem("block_of_tungsten", BLOCK_OF_TUNGSTEN);

        public static final DeferredBlock<Block> BLOCK_OF_RAW_TUNGSTEN = BLOCKS
                        .registerSimpleBlock("block_of_raw_tungsten",
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)
                                                        .requiresCorrectToolForDrops());

        public static final DeferredItem<BlockItem> BLOCK_OF_RAW_TUNGSTEN_ITEM = ITEMS
                        .registerSimpleBlockItem("block_of_raw_tungsten", BLOCK_OF_RAW_TUNGSTEN);

        // 1. THE CUSTOM 1x2 FRUIT BLOCK
        public static final DeferredBlock<Block> CRYSTALLINE_MELON = BLOCKS.register("crystalline_melon",
                        () -> new CrystallineMelon(BlockBehaviour.Properties.ofFullCopy(Blocks.MELON)));

        public static final DeferredItem<BlockItem> CRYSTALLINE_MELON_ITEM = ITEMS
                        .registerSimpleBlockItem("crystalline_melon", CRYSTALLINE_MELON);

        private static DeferredBlock<Block> registerBalloon(String color, DyeColor dyeColor) {
                DeferredBlock<Block> block = BLOCKS.register(color + "_balloon",
                                () -> new BalloonBlock(dyeColor));

                ITEMS.registerSimpleBlockItem(color + "_balloon", block);
                return block;
        }

        public static final DeferredBlock<Block> WHITE_BALLOON = registerBalloon("white", DyeColor.WHITE);
        public static final DeferredBlock<Block> RED_BALLOON = registerBalloon("red", DyeColor.RED);
        public static final DeferredBlock<Block> BLUE_BALLOON = registerBalloon("blue", DyeColor.BLUE);
        public static final DeferredBlock<Block> PINK_BALLOON = registerBalloon("pink", DyeColor.PINK);
        public static final DeferredBlock<Block> GREEN_BALLOON = registerBalloon("green", DyeColor.GREEN);
        public static final DeferredBlock<Block> YELLOW_BALLOON = registerBalloon("yellow", DyeColor.YELLOW);
        public static final DeferredBlock<Block> ORANGE_BALLOON = registerBalloon("orange", DyeColor.ORANGE);
        public static final DeferredBlock<Block> PURPLE_BALLOON = registerBalloon("purple", DyeColor.PURPLE);
        public static final DeferredBlock<Block> BLACK_BALLOON = registerBalloon("black", DyeColor.BLACK);
        public static final DeferredBlock<Block> GRAY_BALLOON = registerBalloon("gray", DyeColor.GRAY);
        public static final DeferredBlock<Block> BROWN_BALLOON = registerBalloon("brown", DyeColor.BROWN);
        public static final DeferredBlock<Block> LIME_BALLOON = registerBalloon("lime", DyeColor.LIME);
        public static final DeferredBlock<Block> CYAN_BALLOON = registerBalloon("cyan", DyeColor.CYAN);
        public static final DeferredBlock<Block> MAGENTA_BALLOON = registerBalloon("magenta", DyeColor.MAGENTA);
        public static final DeferredBlock<Block> LIGHT_BLUE_BALLOON = registerBalloon("light_blue",
                        DyeColor.LIGHT_BLUE);
        public static final DeferredBlock<Block> LIGHT_GRAY_BALLOON = registerBalloon("light_gray",
                        DyeColor.LIGHT_GRAY);

        public static final DeferredItem<Item> TUNGSTEN_INGOT = ITEMS.registerSimpleItem("tungsten_ingot");

        public static final DeferredItem<Item> RAW_TUNGSTEN = ITEMS.registerSimpleItem("raw_tungsten");

        public static final DeferredItem<Item> BOSAMBO_SADDLE = ITEMS.registerSimpleItem("bosambo_saddle");

        // 2. THE ATTACHED STEM BLOCK
        public static final DeferredBlock<Block> ATTACHED_CRYSTALLINE_STEM = BLOCKS.register(
                        "attached_crystalline_stem",
                        () -> new net.minecraft.world.level.block.AttachedStemBlock(
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.BLOCK,
                                                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                                                        MODID, "crystalline_stem")),
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.BLOCK,
                                                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                                                        MODID, "crystalline_melon")),
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.ITEM,
                                                        net.minecraft.resources.ResourceLocation
                                                                        .fromNamespaceAndPath(MODID, "shiny_seeds")),
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.ATTACHED_MELON_STEM)));

        // 3. THE CUSTOM 1x2 STEM BLOCK
        public static final DeferredBlock<Block> CRYSTALLINE_STEM = BLOCKS.register("crystalline_stem",
                        () -> new CrystallineStem(
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.BLOCK,
                                                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                                                        MODID, "crystalline_melon")),
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.BLOCK,
                                                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                                                        MODID,
                                                                        "attached_crystalline_stem")),
                                        net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.ITEM,
                                                        net.minecraft.resources.ResourceLocation
                                                                        .fromNamespaceAndPath(MODID, "shiny_seeds")),
                                        CRYSTALLINE_MELON,
                                        ATTACHED_CRYSTALLINE_STEM,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.MELON_STEM)));

        public static final DeferredItem<Item> SHINY_SEEDS = ITEMS.register("shiny_seeds",
                        () -> new net.minecraft.world.item.ItemNameBlockItem(CRYSTALLINE_STEM.get(),
                                        new Item.Properties()));

        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_DIG = SOUNDS.register("bosambo_dig",
                        () -> SoundEvent.createVariableRangeEvent(
                                        ResourceLocation.fromNamespaceAndPath(MODID, "bosambo_dig")));
        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_DIG_STOP = SOUNDS.register(
                        "bosambo_dig_stop",
                        () -> SoundEvent
                                        .createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID,
                                                        "bosambo_dig_stop")));

        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_AMBIENT = SOUNDS.register("bosambo_ambient",
                        () -> SoundEvent.createVariableRangeEvent(
                                        ResourceLocation.fromNamespaceAndPath(MODID, "bosambo_ambient")));

        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_HURT = SOUNDS.register("bosambo_hurt",
                        () -> SoundEvent.createVariableRangeEvent(
                                        ResourceLocation.fromNamespaceAndPath(MODID, "bosambo_hurt")));

        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_DEATH = SOUNDS.register("bosambo_death",
                        () -> SoundEvent.createVariableRangeEvent(
                                        ResourceLocation.fromNamespaceAndPath(MODID, "bosambo_death")));

        public static final DeferredHolder<SoundEvent, SoundEvent> BOSAMBO_STEP = SOUNDS.register("bosambo_step",
                        () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID,
                                        "bosambo_step")));

        public static final DeferredBlock<Block> BOSAMBO_EGG_BLOCK = BLOCKS.register("bosambo_egg",
                        () -> new BosamboEggBlock(
                                        BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.5F)
                                                        .randomTicks().noOcclusion()));

        public static final DeferredItem<BlockItem> BOSAMBO_EGG_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                        "bosambo_egg",
                        BOSAMBO_EGG_BLOCK);

        public static final DeferredItem<Item> CRYSTALLINE_MELON_SLICE = ITEMS
                        .registerSimpleItem("crystalline_melon_slice");

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
                        .register("example_tab", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.justacreature"))
                                        .withTabsBefore(CreativeModeTabs.COMBAT)
                                        .icon(() -> BOSAMBO_EGG_BLOCK_ITEM.get().getDefaultInstance())
                                        .displayItems((parameters, output) -> {
                                                output.accept(JustaCreatureMobs.BOSAMBO_SPAWN_EGG.get());
                                                output.accept(BOSAMBO_EGG_BLOCK_ITEM.get());
                                                output.accept(CRYSTALLINE_MELON_SLICE.get());
                                                output.accept(SHINY_SEEDS.get());
                                                output.accept(CRYSTALLINE_MELON_ITEM.get());
                                                output.accept(TUNGSTEN_INGOT.get());
                                                output.accept(RAW_TUNGSTEN.get());
                                                output.accept(DEEPSLATE_TUNGSTEN_ORE.get());
                                                output.accept(BLOCK_OF_TUNGSTEN.get());
                                                output.accept(BLOCK_OF_RAW_TUNGSTEN.get());
                                                output.accept(BOSAMBO_SADDLE.get());
                                                output.accept(WHITE_BALLOON.get());
                                                output.accept(RED_BALLOON.get());
                                                output.accept(BLUE_BALLOON.get());
                                                output.accept(PINK_BALLOON.get());
                                                output.accept(GREEN_BALLOON.get());
                                                output.accept(YELLOW_BALLOON.get());
                                                output.accept(ORANGE_BALLOON.get());
                                                output.accept(PURPLE_BALLOON.get());
                                                output.accept(BLACK_BALLOON.get());
                                                output.accept(GRAY_BALLOON.get());
                                                output.accept(BROWN_BALLOON.get());
                                                output.accept(LIME_BALLOON.get());
                                                output.accept(CYAN_BALLOON.get());
                                                output.accept(MAGENTA_BALLOON.get());
                                                output.accept(LIGHT_BLUE_BALLOON.get());
                                                output.accept(LIGHT_GRAY_BALLOON.get());
                                        }).build());

        public JustaCreature(IEventBus modEventBus, ModContainer modContainer) {
                modEventBus.addListener(this::commonSetup);

                BLOCKS.register(modEventBus);
                ITEMS.register(modEventBus);
                CREATIVE_MODE_TABS.register(modEventBus);
                SOUNDS.register(modEventBus);
                JustaCreatureMobs.ENTITY_TYPES.register(modEventBus);
                JustaCreatureMobs.ITEMS.register(modEventBus);

                NeoForge.EVENT_BUS.register(this);

                modEventBus.addListener(this::addCreative);

                modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        }

        private void commonSetup(FMLCommonSetupEvent event) {
                LOGGER.info("HELLO FROM COMMON SETUP");

                if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
                        LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
                }

                LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

                Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
                }
        }

        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
                LOGGER.info("HELLO from server starting");
        }

}
