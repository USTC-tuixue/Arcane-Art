package com.ustctuixue.arcaneart.gui.magicmenu;

import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.networking.KeyPack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.util.LazyOptional;

public class MagicContainer extends Container {
	private PlayerEntity player;
	private PlayerInventory inv;
	private Inventory spellMainInventory;
	private Inventory spellHotBar;
	private Inventory deleteIcon;
	private Inventory itchSpellSlot;

	public MagicContainer(int id, PlayerInventory inv, PlayerEntity player) {
		super(ContainerTypeRegistry.magicContainer.get(), id);
		this.inv = inv;
		this.player = player;
		LazyOptional<ISpellInventory> spellCap = player
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
		spellCap.ifPresent((spellInventory) -> {
			this.spellMainInventory = spellInventory.getInner();
			this.spellHotBar = spellInventory.getShortCut();
		});
		layoutPlayerInventorySlots(inv, 8, 140);
		this.itchSpellSlot = new Inventory(1);
		this.deleteIcon = new Inventory(1);
		addSlotBox(spellMainInventory, 0, 8, 18, 9, 18, 6, 18);
		addSlotBox(spellHotBar, 0, 173, 18, 1, 18, 9, 18);
		addSlot(new Slot(deleteIcon, 0, 199, 95));
		addSlot(new Slot(itchSpellSlot, 0, 199, 62));
	}

	public int getEmptySlot() {
		for (int i = 0; i < 54; i++) {
			if (spellMainInventory.getStackInSlot(i) == ItemStack.EMPTY) {
				return i + 36;
			}
		}
		for (int i = 0; i < 9; i++) {
			if (spellHotBar.getStackInSlot(i) == ItemStack.EMPTY) {
				return i + 90;
			}
		}
		return -1;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ItemStack.EMPTY;
	}

	private void layoutPlayerInventorySlots(IInventory inventory, int leftCol, int topRow) {
		// Player inventory
		addSlotBox(inventory, 9, leftCol, topRow, 9, 18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(inventory, 0, leftCol, topRow, 9, 18);
	}

	private int addSlotBox(IInventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount,
			int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = addSlotRange(inventory, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		if (slotId == -999) {
			if (player.inventory.getItemStack().getItem() instanceof ItemSpell) {
				return ItemStack.EMPTY;
			} else {
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}
		}
		if (slotId >= 0) {
			ItemStack pickedItemStack = getSlot(slotId).getStack();
			ItemStack mouseItemStack = player.inventory.getItemStack();

			if (!(pickedItemStack.isEmpty() || pickedItemStack.getItem() instanceof ItemSpell
					|| pickedItemStack.getItem() == Items.WRITTEN_BOOK)) {
				return ItemStack.EMPTY;
			}

			if (mouseItemStack.getItem() == Items.WRITTEN_BOOK) {
				if (!(slotId < 36 || slotId == 100)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotId == 99 && mouseItemStack.getItem() instanceof ItemSpell) {
				player.inventory.setItemStack(ItemStack.EMPTY);
				return ItemStack.EMPTY;
			}
			if (mouseItemStack.getItem() instanceof ItemSpell) {
				if (!(slotId >= 36 && slotId < 99)) {
					return ItemStack.EMPTY;
				}
			}
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public void onContainerClosed(PlayerEntity playerIn) {
		if (itchSpellSlot.getStackInSlot(0) != ItemStack.EMPTY) {
			playerIn.dropItem(itchSpellSlot.getStackInSlot(0), false);
		}
		PlayerInventory playerinventory = playerIn.inventory;
		if (!playerinventory.getItemStack().isEmpty()) {
			if (playerinventory.getItemStack().getItem() instanceof ItemSpell) {
				int k = getEmptySlot();
				if (k != -1) {
					slotClick(k, 0, ClickType.PICKUP, playerIn);
				}
			} else {
				playerIn.dropItem(playerinventory.getItemStack(), false);

				playerinventory.setItemStack(ItemStack.EMPTY);
			}
		}
	}

	@Override
	public boolean canDragIntoSlot(Slot slotIn) {
		return slotIn.inventory != spellMainInventory && slotIn.inventory != spellHotBar && slotIn.inventory != deleteIcon;
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
