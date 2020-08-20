package com.ustctuixue.arcaneart.api.spell.entityspellball;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class EntitySpellBallRenderer extends EntityRenderer<EntitySpellBall> {
    private EntityModel<EntitySpellBall> SpellModel;

    public EntitySpellBallRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        SpellModel = new SpellBallModel();
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySpellBall entity) {
        return new ResourceLocation(ArcaneArt.MOD_ID, "textures/entity/spell_ball.png");
    }

    @Override
    public void render(EntitySpellBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.SpellModel.getRenderType(this.getEntityTexture(entityIn)));
        this.SpellModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }



}
