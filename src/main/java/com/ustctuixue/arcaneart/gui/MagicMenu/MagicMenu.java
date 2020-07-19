package com.ustctuixue.arcaneart.gui.MagicMenu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ustctuixue.arcaneart.ArcaneArt;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MagicMenu extends ContainerScreen<MagicContainer>{
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/container/magic_menu.png");
	public MagicMenu(MagicContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.xSize=225;
		this.ySize=221;
	}
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX,int mouseY) {
		this.font.drawString(new TranslationTextComponent("title.arcaneart.magicmenu").getFormattedText(),8.0f,6.0f,0xAB61AB);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
	}
	@Override
	public void render(int mouseX,int mouseY,float partialTicks) {
		this.renderBackground();
		super.render(mouseX,mouseY,partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		assert minecraft !=null;
		minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
