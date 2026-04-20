package com.pandagamerplayzz.createcreative.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pandagamerplayzz.createcreative.CCPartialModels;
import com.pandagamerplayzz.createcreative.block.entity.VoidPumpBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class VoidPumpRenderer extends SafeBlockEntityRenderer<VoidPumpBlockEntity> {
    public VoidPumpRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(VoidPumpBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();

        SuperByteBuffer cog = CachedBuffers.partialFacing(CCPartialModels.VOID_PUMP_COG, state);

        KineticBlockEntityRenderer.standardKineticRotationTransform(cog, be, light)
            .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }
}