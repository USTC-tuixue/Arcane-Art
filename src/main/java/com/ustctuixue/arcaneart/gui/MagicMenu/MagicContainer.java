package com.ustctuixue.arcaneart.gui.MagicMenu;

import javax.annotation.Nullable;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;

public class MagicContainer extends Container{
	private PlayerEntity player;
	private PlayerInventory inv;
	private Inventory Inv1;
	private Inventory Inv2;
	public MagicContainer( int id,PlayerInventory inv,PlayerEntity player) {
		super(ContainerTypeRegistry.magicContainer.get(), id);
		this.inv=inv;
		this.player=player;
        LazyOptional<ISpellInventory> spellCap = player.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
        spellCap.ifPresent((l) -> {
        	this.Inv1=l.getInner();
        	this.Inv2=l.getShortCut();
            }
        );
        layoutPlayerInventorySlots(inv, 8, 140);
	}
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

    private void layoutPlayerInventorySlots(IInventory inventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(inventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(inventory, 0, leftCol, topRow, 9, 18);
    }
    private int addSlotBox(IInventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
    private int addSlotRange(IInventory inventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
}
