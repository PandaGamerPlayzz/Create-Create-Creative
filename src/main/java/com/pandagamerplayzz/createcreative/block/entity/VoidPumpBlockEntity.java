package com.pandagamerplayzz.createcreative.block.entity;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class VoidPumpBlockEntity extends PumpBlockEntity {
    private static final float MAX_VOID_WATER_RATE_MB_PER_SEC = 1f / 60f;
    private static final float FULL_RATE_RPM = 256.0f;

    private float generatedRemainder;

    public VoidPumpBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) com.pandagamerplayzz.createcreative.CreateCreative.VOID_PUMP_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide)
            return;

        if (getSpeed() == 0)
            return;

        if (!isIntakeBorderingVoid())
            return;

        pumpWaterToOutput();
    }

    @Nullable
    private Direction getPullDirection() {
        Direction front = getFront();
        if (front == null)
            return null;

        // PumpBlockEntity pulls from the back and outputs to the front
        return front.getOpposite();
    }

    @Nullable
    private Direction getOutputDirection() {
        return getFront();
    }

    private boolean isIntakeBorderingVoid() {
        Direction pullDir = getPullDirection();
        if (pullDir == null)
            return false;

        if (pullDir != Direction.DOWN)
            return false;

        int minY = level.getMinBuildHeight();
        BlockPos.MutableBlockPos cursor = worldPosition.below().mutable();

        while (cursor.getY() >= minY) {
            BlockState state = level.getBlockState(cursor);

            if (!state.isAir())
                return false;

            if (!level.getFluidState(cursor).isEmpty())
                return false;

            cursor.move(Direction.DOWN);
        }

        return true;
    }

    private void pumpWaterToOutput() {
        Direction outputDir = getOutputDirection();
        if (outputDir == null)
            return;

        BlockPos outputPos = worldPosition.relative(outputDir);
        BlockEntity outputBE = level.getBlockEntity(outputPos);
        if (outputBE == null)
            return;

        IFluidHandler handler = level.getCapability(
                Capabilities.FluidHandler.BLOCK,
                outputPos,
                outputDir.getOpposite()
        );

        if (handler == null)
            return;

        float mbPerTick = getCurrentMbPerTick();
        if (mbPerTick <= 0)
            return;

        generatedRemainder += mbPerTick;

        int wholeMb = (int) generatedRemainder;
        if (wholeMb <= 0)
            return;

        FluidStack toFill = new FluidStack(Fluids.WATER, wholeMb);
        int filled = handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);

        if (filled > 0) {
            generatedRemainder -= filled;
            notifyUpdate();
        }
    }

    private float getCurrentMbPerTick() {
        float speed = Math.abs(getSpeed());
        float speedMultiplier = Math.min(speed / FULL_RATE_RPM, 1.0f);
        float mbPerSecond = MAX_VOID_WATER_RATE_MB_PER_SEC * speedMultiplier;
        return mbPerSecond / 20.0f;
    }
}