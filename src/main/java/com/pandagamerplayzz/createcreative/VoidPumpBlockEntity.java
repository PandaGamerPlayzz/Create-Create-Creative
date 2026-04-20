package com.pandagamerplayzz.createcreative;

import net.createmod.catnip.data.Couple;
import org.apache.commons.lang3.mutable.MutableBoolean;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VoidPumpBlockEntity extends KineticBlockEntity {

    Couple<MutableBoolean> sidesToUpdate;
    boolean pressureUpdate;
    boolean scheduleFlip;

    public VoidPumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.VOID_PUMP.get(), pos, state);
        this.sidesToUpdate = Couple.create(MutableBoolean::new);
    }
}