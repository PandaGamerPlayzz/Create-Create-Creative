package com.pandagamerplayzz.createcreative;

import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class ModPartialModels {
    private static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath("create_create_creative", path);
    }

    private static PartialModel block(String path) {
        return PartialModel.of(asResource("block/" + path));
    }

    public static final PartialModel VOID_PUMP_COG = block("void_pump/cog");

    public static void init() {
    }
}