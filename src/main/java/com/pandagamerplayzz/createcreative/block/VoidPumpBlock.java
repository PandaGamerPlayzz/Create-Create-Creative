package com.pandagamerplayzz.createcreative.block;

import com.pandagamerplayzz.createcreative.CreateCreative;
import com.pandagamerplayzz.createcreative.block.entity.VoidPumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.data.SharedProperties;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class VoidPumpBlock extends PumpBlock {
    public VoidPumpBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(SharedProperties.copperMetal())
                .mapColor(MapColor.STONE));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<PumpBlockEntity> getBlockEntityClass() {
        return (Class<PumpBlockEntity>) (Class<?>) VoidPumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return CreateCreative.VOID_PUMP_BLOCK_ENTITY.get();
    }
}