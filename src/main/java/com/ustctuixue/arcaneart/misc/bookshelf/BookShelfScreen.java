package com.ustctuixue.arcaneart.misc.bookshelf;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BookShelfScreen extends ContainerScreen<BookShelfContainer> {
    private ResourceLocation BOOKSHELF_GUI = new ResourceLocation("arcaneart", "textures/gui/container/bookshelf.png");
    private int textureWidth = 176;
    private int textureHeight = 151;

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    public BookShelfScreen(BookShelfContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(new TranslationTextComponent("title.arcaneart.bookshelf").getFormattedText(),8.0f,6.0f,0xAB61AB);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(BOOKSHELF_GUI);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
