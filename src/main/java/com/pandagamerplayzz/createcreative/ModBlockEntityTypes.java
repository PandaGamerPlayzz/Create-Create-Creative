package com.pandagamerplayzz.createcreative;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateCreative.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VoidPumpBlockEntity>> VOID_PUMP =
            BLOCK_ENTITY_TYPES.register("void_pump",
                    () -> BlockEntityType.Builder.of(
                            VoidPumpBlockEntity::new,
                            CreateCreative.VOID_PUMP.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}