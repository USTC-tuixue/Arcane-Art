package com.ustctuixue.arcaneart.api.spell.inventory;

import java.util.List;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class SpellInventory implements ISpellInventory {
	private Inventory inner;
	private Inventory shortcut;

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT r = new CompoundNBT();
		ListNBT listnbt1 = new ListNBT();
		ListNBT listnbt2 = new ListNBT();
		for (int i = 0; i < 54; i++) {
			CompoundNBT compoundnbt = new CompoundNBT();
			this.inner.getStackInSlot(i).write(compoundnbt);
			listnbt1.add(compoundnbt);
		}

		for (int i = 0; i < 9; i++) {
			CompoundNBT compoundnbt = new CompoundNBT();
			this.shortcut.getStackInSlot(i).write(compoundnbt);
			listnbt2.add(compoundnbt);
		}
		r.put("inner", listnbt1);
		r.put("shortcut", listnbt2);
		return r;
	}

	public SpellInventory(Inventory inner, Inventory shortcut) {
		this.inner = inner;
		this.shortcut = shortcut;
	}

	public SpellInventory() {
		this.inner = new Inventory(54);
		this.shortcut = new Inventory(9);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.inner = new Inventory(54);
		this.shortcut = new Inventory(9);
		ListNBT list1 = nbt.getList("inner", Constants.NBT.TAG_COMPOUND);
		ListNBT list2 = nbt.getList("shortcut", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 54; i++) {
			this.inner.setInventorySlotContents(i, ItemStack.read(list1.getCompound(i)));
		}
		for (int i = 0; i < 9; i++) {
			this.shortcut.setInventorySlotContents(i, ItemStack.read(list2.getCompound(i)));
		}
	}

	@Override
	public ItemStack getItemStack(int index) {
		return (index < 54) ? this.inner.getStackInSlot(index) : ItemStack.EMPTY;
	}

	@Override
	public void writeItemStack(ItemStack target, int index) {
		if (index < 54)
			this.inner.setInventorySlotContents(index, target);
	}

	@Override
	public ItemStack getShortcut(int index) {
		return (index < 9) ? this.shortcut.getStackInSlot(index) : ItemStack.EMPTY;
	}

	@Override
	public void writeShortcut(ItemStack target, int index) {
		if (index < 9)
			this.shortcut.setInventorySlotContents(index, target);
	}

	@Override
	public Inventory getInner() {
		return this.inner;
	}

	@Override
	public Inventory getShortCut() {
		return this.shortcut;
	}
}
