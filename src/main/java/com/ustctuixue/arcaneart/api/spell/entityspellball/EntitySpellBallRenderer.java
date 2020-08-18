package com.ustctuixue.arcaneart.api.spell.entityspellball;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntitySpellBallRenderer extends EntityRenderer<EntitySpellBall> {
    public EntitySpellBallRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySpellBall entity) {
        return new ResourceLocation("arcaneart", "textures/entity/spell_ball.png");
    }

    @Override
    public void render(EntitySpellBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

}
