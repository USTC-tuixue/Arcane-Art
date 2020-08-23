package com.ustctuixue.arcaneart.ritual.device;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nonnull;

public class DingTileEntityRenderer extends TileEntityRenderer<DingTileEntity> {
    public DingTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DingTileEntity tileEntityIn, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemStack = tileEntityIn.getItemStored();
        if(itemStack.isEmpty()) {
            return;
        }
        matrixStackIn.push();

        matrixStackIn.translate(1F, 1.2F, 1F);
        if(itemStack.getItem() instanceof BlockItem) {
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
            BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
            BlockState state = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
            blockRenderer.renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
        } else {
            matrixStackIn.rotate(new Quaternion(25F, 1, 0, 0));
            matrixStackIn.rotate(new Quaternion(25F, 0, 1, 0));
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel iBakedModel = itemRenderer.getItemModelWithOverrides(itemStack, tileEntityIn.getWorld(), null);
            itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, iBakedModel);
        }
        
        matrixStackIn.pop();
    }
}
