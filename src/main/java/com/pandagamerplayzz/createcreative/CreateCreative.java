package com.pandagamerplayzz.createcreative;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CreateCreative.MODID)
public class CreateCreative {
    public static final String MODID = "create_create_creative";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredBlock<Block> VOID_PUMP = BLOCKS.register(
            "void_pump",
            () -> new VoidPumpBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.0f, 6.0f)
            )
    );

    public static final DeferredItem<Item> VOID_PUMP_ITEM = ITEMS.register(
            "void_pump",
            () -> new BlockItem(VOID_PUMP.get(), new Item.Properties())
    );

    // Keep this ONLY while testing the cog item model directly
    public static final DeferredItem<Item> VOID_PUMP_COG_TEST = ITEMS.register(
            "void_pump_cog_test",
            () -> new Item(new Item.Properties())
    );

    public CreateCreative(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Create Creative common setup");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(VOID_PUMP_ITEM);
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(VOID_PUMP_COG_TEST);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Create Creative server starting");
    }

    @EventBusSubscriber(modid = CreateCreative.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Create Creative client setup");
            ModPartialModels.init();

            event.enqueueWork(() -> {
                BlockEntityRenderers.register(ModBlockEntityTypes.VOID_PUMP.get(), VoidPumpRenderer::new);
            });
        }
    }
}