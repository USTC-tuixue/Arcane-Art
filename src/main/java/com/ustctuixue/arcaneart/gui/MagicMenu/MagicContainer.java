package com.ustctuixue.arcaneart.gui.MagicMenu;

import javax.annotation.Nullable;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;

public class MagicContainer extends Container{
	private PlayerEntity player;
	private PlayerInventory inv;
	private Inventory Inv1;
	private Inventory Inv2;
	private Inventory Btn1;
	private Inventory Btn2;
	private Inventory sec;
	
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
        this.sec=new Inventory(1);
        this.Btn1=new Inventory(1);
        this.Btn2=new Inventory(1);
        addSlotBox(Inv1,0,8,18,9,18,6,18);
        addSlotBox(Inv2,0,173,18,1,18,9,18);
        addSlot(new Slot(Btn1, 0, 199, 44));
        addSlot(new Slot(Btn2, 0, 199, 95));
        addSlot(new Slot(sec, 0, 199, 62));
	}
	public int getEmptySlot() {
		for(int i=0;i<54;i++)
			if(Inv1.getStackInSlot(i)==ItemStack.EMPTY)return i+36;
		for(int i=0;i<9;i++)
			if(Inv2.getStackInSlot(i)==ItemStack.EMPTY)return i+90;
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
    private int addSlotBox(IInventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    	if(slotId==-999)
    		if(player.inventory.getItemStack().getItem() instanceof ItemSpell) {
    			return ItemStack.EMPTY;
    		}else {
    			if(player.inventory.getItemStack()!=ItemStack.EMPTY) {
    				player.dropItem(player.inventory.getItemStack(), false);
    				player.inventory.setItemStack(ItemStack.EMPTY);
    			}
    		}
    	
    	if(slotId>=0) {
    	ItemStack itm=getSlot(slotId).getStack();
    	ItemStack itm1=player.inventory.getItemStack();
    	if(!(itm.isEmpty()||itm.getItem() instanceof ItemSpell||itm.getItem()==Items.WRITTEN_BOOK))return ItemStack.EMPTY;
    	if(itm1.getItem()==Items.WRITTEN_BOOK)
    		if(!(slotId>=0&&slotId<36||slotId==101))return ItemStack.EMPTY;
        if(slotId==100&&itm1.getItem() instanceof ItemSpell) {
        	player.inventory.setItemStack(ItemStack.EMPTY);
        	return ItemStack.EMPTY;
        }
        if(itm1.getItem() instanceof ItemSpell)
        	if(!(slotId>=36&&slotId<99))return ItemStack.EMPTY;
    	return super.slotClick(slotId, dragType, clickTypeIn, player);
    	}
    	return ItemStack.EMPTY;
     }
    public void onContainerClosed(PlayerEntity playerIn) {
    	if(sec.getStackInSlot(0)!=ItemStack.EMPTY) {
    		playerIn.dropItem(sec.getStackInSlot(0), false);
    	}  
    	PlayerInventory playerinventory = playerIn.inventory;
        if (!playerinventory.getItemStack().isEmpty()) {
            if(playerinventory.getItemStack().getItem() instanceof ItemSpell) {
            	int k=getEmptySlot();
            	if(k!=-1) {
            		slotClick(k,0,ClickType.PICKUP,playerIn);
            	}
            }else {
        	playerIn.dropItem(playerinventory.getItemStack(), false);
            
            playerinventory.setItemStack(ItemStack.EMPTY);
            }
         }
    }
    @Override
    public boolean canDragIntoSlot(Slot slotIn) {
    	if(slotIn.inventory==Inv1||slotIn.inventory==Inv2||slotIn.inventory==Btn1||slotIn.inventory==Btn2)return false;
        return true;
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
