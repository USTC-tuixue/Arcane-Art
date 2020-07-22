package com.ustctuixue.arcaneart.gui.hud.Manabar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ustctuixue.arcaneart.ArcaneArt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class ManaBar extends AbstractGui{
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final ResourceLocation HUDC = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/hudc.png");
    private final ResourceLocation HUD = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/hud.png");
    private double percent=0;
    public ManaBar(double percent) {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.percent=percent;
    }

    public void render() {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(HUDC);
        blit(2 , height/2  - 60+(int)(120*(1-percent)), 0, (int)(120*(1-percent)), 8, 120-(int)(120*(1-percent)), 8, 120);
        this.minecraft.getTextureManager().bindTexture(HUD);
        blit(2 , height/2  - 60, 0, 0, 8, 120, 8, 120);
    }
}
