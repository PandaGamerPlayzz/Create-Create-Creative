package com.pandagamerplayzz.createcreative.block.entity;

import com.simibubi.create.content.fluids.pump.PumpBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VoidPumpBlockEntity extends PumpBlockEntity {
    public VoidPumpBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) com.pandagamerplayzz.createcreative.CreateCreative.VOID_PUMP_BLOCK_ENTITY.get(), pos, state);
    }
}