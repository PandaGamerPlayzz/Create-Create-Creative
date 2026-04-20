package com.pandagamerplayzz.createcreative;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.pandagamerplayzz.createcreative.block.VoidPumpBlock;
import com.pandagamerplayzz.createcreative.block.entity.VoidPumpBlockEntity;
import com.pandagamerplayzz.createcreative.block.entity.renderer.VoidPumpRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CreateCreative.MODID)
public class CreateCreative {
    public static final String MODID = "create_create_creative";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredBlock<Block> VOID_PUMP = BLOCKS.register("void_pump", VoidPumpBlock::new);

    public static final DeferredItem<Item> VOID_PUMP_ITEM = ITEMS.register("void_pump",
            () -> new BlockItem(VOID_PUMP.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VoidPumpBlockEntity>> VOID_PUMP_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("void_pump",
                    () -> BlockEntityType.Builder.of(VoidPumpBlockEntity::new, VOID_PUMP.get()).build(null));

    public CreateCreative(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Create Creative common setup");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(VOID_PUMP_ITEM);
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

            event.enqueueWork(() ->
                    BlockEntityRenderers.register(VOID_PUMP_BLOCK_ENTITY.get(), VoidPumpRenderer::new)
            );
        }
    }
}

//package com.pandagamerplayzz.createcreative;
//
//import org.slf4j.Logger;
//
//import com.mojang.logging.LogUtils;
//
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.CreativeModeTabs;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.material.MapColor;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.ModContainer;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.fml.common.Mod;
//import net.neoforged.fml.config.ModConfig;
//import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.neoforged.neoforge.common.NeoForge;
//import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
//import net.neoforged.neoforge.event.server.ServerStartingEvent;
//import net.neoforged.neoforge.registries.DeferredBlock;
//import net.neoforged.neoforge.registries.DeferredItem;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//@Mod(CreateCreative.MODID)
//public class CreateCreative {
//    public static final String MODID = "create_create_creative";
//    public static final Logger LOGGER = LogUtils.getLogger();
//
//    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
//    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
//
//    public CreateCreative(IEventBus modEventBus, ModContainer modContainer) {
//        modEventBus.addListener(this::commonSetup);
//        modEventBus.addListener(this::addCreative);
//
//        NeoForge.EVENT_BUS.register(this);
//
//        BLOCKS.register(modEventBus);
//        ITEMS.register(modEventBus);
//
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
//    }
//
//    private void commonSetup(FMLCommonSetupEvent event) {
//        LOGGER.info("Create Creative common setup");
//    }
//
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//
//    }
//
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        LOGGER.info("Create Creative server starting");
//    }
//
//    @EventBusSubscriber(modid = CreateCreative.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class ClientModEvents {
//        @SubscribeEvent
//        public static void onClientSetup(FMLClientSetupEvent event) {
//            LOGGER.info("Create Creative client setup");
//        }
//    }
//}