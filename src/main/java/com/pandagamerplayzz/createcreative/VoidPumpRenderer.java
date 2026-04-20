//package com.pandagamerplayzz.createcreative;
//
//import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
//import com.simibubi.create.AllPartialModels;
//
//import net.createmod.catnip.render.CachedBuffers;
//import net.createmod.catnip.render.SuperByteBuffer;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class VoidPumpRenderer extends KineticBlockEntityRenderer<VoidPumpBlockEntity> {
//
//    public VoidPumpRenderer(BlockEntityRendererProvider.Context context) {
//        super(context);
//    }
//
//    @Override
//    protected SuperByteBuffer getRotatedModel(VoidPumpBlockEntity be, BlockState state) {
//        return CachedBuffers.partialFacing(ModPartialModels.VOID_PUMP_COG, state);
//    }
//}

package com.pandagamerplayzz.createcreative;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class VoidPumpRenderer implements BlockEntityRenderer<VoidPumpBlockEntity> {

    public VoidPumpRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(VoidPumpBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        BlockState state = be.getBlockState();

        SuperByteBuffer cog = CachedBuffers.partialFacing(ModPartialModels.VOID_PUMP_COG, state);

        cog.light(packedLight)
                .renderInto(poseStack, buffer.getBuffer(Sheets.cutoutBlockSheet()));
    }
}