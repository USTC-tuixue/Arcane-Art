package com.ustctuixue.arcaneart.gui.MagicMenu;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.types.templates.List;
import com.ustctuixue.arcaneart.ArcaneArt;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MagicMenu extends ContainerScreen<MagicContainer>
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/container/magic_menu.png");

    public MagicMenu(MagicContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.xSize = 225;
        this.ySize = 221;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(new TranslationTextComponent("title.arcaneart.magicmenu").getFormattedText(), 8.0f, 6.0f, 0xAB61AB);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
        if (isPointInRegion(199, 44, 16, 16, mouseX, mouseY))
        {
            ArrayList<String> tooltipList = new ArrayList<String>()
            {{
                add(new TranslationTextComponent("info.arcaneart.itchspell").getFormattedText());
            }};
            renderTooltip(tooltipList, mouseX - guiLeft, mouseY - guiTop);
        }
        if (isPointInRegion(199, 95, 16, 16, mouseX, mouseY))
        {
            ArrayList<String> tooltipList = new ArrayList<String>()
            {{
                add(new TranslationTextComponent("info.arcaneart.deletespell").getFormattedText());
            }};
            renderTooltip(tooltipList, mouseX - guiLeft, mouseY - guiTop);
        }
        minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        for (int i = 0; i < 9; i++)
        {
            ItemStack p = this.container.inventorySlots.get(i + 27).getStack();
            if (!(p.isEmpty() || p.getItem() == Items.WRITTEN_BOOK))
            {
                this.blit(8 + 18 * i, 198, 225, 0, 16, 16);
            }
        }
        for (int i = 0; i < 27; i++)
        {
            ItemStack p = this.container.inventorySlots.get(i).getStack();
            if (!(p.isEmpty() || p.getItem() == Items.WRITTEN_BOOK))
            {
                this.blit(8 + 18 * (i % 9), 140 + 18 * (i / 9), 225, 0, 16, 16);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert minecraft != null;
        minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
