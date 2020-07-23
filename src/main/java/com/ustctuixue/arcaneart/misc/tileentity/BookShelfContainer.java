package com.ustctuixue.arcaneart.misc.tileentity;

import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.misc.block.BookShelf;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookShelfContainer extends Container {
	Inventory book;
	BlockPos thisis;
	World where;
    public BookShelfContainer(int id, PlayerInventory playerInventory, BlockPos pos, World world) {
        super(ContainerTypeRegistry.bookShelfContainer.get(), id);
        BookShelfTileEntity bookShelfTileEntity = (BookShelfTileEntity) world.getTileEntity(pos);
        this.book=bookShelfTileEntity.getInventory();
        layoutPlayerInventorySlots(playerInventory, 8, 69);
        addSlotBox(book,0,44,20,5,18,2,18);
        this.thisis=pos;
        this.where=world;
    }
    void orState(int i,World w,BlockPos b,IntegerProperty s) {
    	if(this.inventorySlots.get(i+36).getStack()!=ItemStack.EMPTY)
    		this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(s, 1));
    	else
    		this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(s, 0));
    }	
    void refreshState() {
        orState(0,this.where,this.thisis,BookShelf.STATE1);
        orState(1,this.where,this.thisis,BookShelf.STATE2);
        orState(2,this.where,this.thisis,BookShelf.STATE3);
        orState(3,this.where,this.thisis,BookShelf.STATE4);
        orState(4,this.where,this.thisis,BookShelf.STATE5);
        orState(5,this.where,this.thisis,BookShelf.STATE6);
        orState(6,this.where,this.thisis,BookShelf.STATE7);
        int c=0;
        for (int i=7;i<10;i++) 
        	if(this.inventorySlots.get(i+36).getStack()!=ItemStack.EMPTY)c++;
        if(c==0) {
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE8, 0));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE9, 0));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATEX, 0));
        }else if(c==1) {
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE8, 0));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE9, 0));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATEX, 1));
        }else if(c==2) {
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE8, 0));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE9, 1));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATEX, 1));
        }else if(c==3) {

        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE8, 1));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATE9, 1));
        	this.where.setBlockState(thisis,this.where.getBlockState(thisis).with(BookShelf.STATEX, 1));
        }
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
	        this.refreshState();
     }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    private int addSlotRange(IInventory inventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
    @Override
    public boolean canDragIntoSlot(Slot slotIn) {
        if(slotIn.inventory==this.book)
        	return false;
        return true;
     }
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    	if(slotId>=36) {
   			if(player.inventory.getItemStack().getItem()!=Items.WRITTEN_BOOK&&!player.inventory.getItemStack().isEmpty())
   				return ItemStack.EMPTY;
    	}
    	return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
    private int addSlotBox(IInventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(IInventory inventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(inventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(inventory, 0, leftCol, topRow, 9, 18);
    }
}
