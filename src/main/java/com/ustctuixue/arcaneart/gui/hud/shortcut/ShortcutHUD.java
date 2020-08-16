package com.ustctuixue.arcaneart.gui.hud.manabar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class ShortcutHUD extends AbstractGui{
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final ResourceLocation HUDFR = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/shortcut.png");
    private final ResourceLocation HUDSEL = new ResourceLocation(ArcaneArt.MOD_ID, "textures/gui/sel.png");
    private final int sel;

    public ShortcutHUD(int sel)
    {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.sel=sel;
    }

    public void render()
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(HUDFR);
        blit(width/2-81, height / 2 - 60, 0, 0, 162, 18,162,18);
        this.minecraft.getTextureManager().bindTexture(HUDSEL);
        blit(width/2-81+18*sel, height / 2 - 60, 0, 0, 18, 18,18,18);
		LazyOptional<ISpellInventory> spellCap = Minecraft.getInstance().player
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
		Inventory inventory = new Inventory();
		inventory=spellCap.orElseGet(SpellInventory::new).getShortCut();
        for(int i=0;i<9;i++) {
        	this.minecraft.getItemRenderer().renderItemIntoGUI(inventory.getStackInSlot(i), width/2-80+18*i, height / 2 - 59);
        }
        if(!inventory.getStackInSlot(sel).isEmpty())
        minecraft.getRenderManager().getFontRenderer().drawString((inventory.getStackInSlot(sel).getItem()).getDisplayName(inventory.getStackInSlot(sel)).getFormattedText(), width/2-80+18*sel, height / 2 - 70,0x790C2F);
    }
}
