package com.ustctuixue.arcaneart.api.spell.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellInventory extends INBTSerializable<CompoundNBT>{
	ItemStack getItemStack(int index);
	void writeItemStack(ItemStack target,int index);
	ItemStack getShortcut(int index);
	void writeShortcut(ItemStack target,int index);
	Inventory getInner();
	Inventory getShortCut();
}
