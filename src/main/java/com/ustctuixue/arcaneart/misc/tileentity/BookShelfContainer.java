package com.ustctuixue.arcaneart.misc.tileentity;

import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.misc.block.BookShelf;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BookShelfContainer extends Container {

	private Inventory book;
	private BlockPos pos;
	private World world;

    public BookShelfContainer(int id, PlayerInventory playerInventory, BlockPos pos, World world) {
        super(ContainerTypeRegistry.bookShelfContainer.get(), id);
        BookShelfTileEntity bookShelfTileEntity = (BookShelfTileEntity) world.getTileEntity(pos);

        assert bookShelfTileEntity != null;

        this.book = bookShelfTileEntity.getInventory();
        layoutPlayerInventorySlots(playerInventory, 8, 69);
        addSlotBox(book,0,44,20,5,18,2,18);
        this.pos = pos;
        this.world = world;
    }

    private void refreshState() {
        BlockState state = this.world.getBlockState(pos);
        for (int i = 0; i < 7; i++)
        {
            state = state.with(BookShelf.BOOKS[i],
                    !this.inventorySlots.get(i + 36).getHasStack());
        }

        int c = 7;
        for (int i = 7; i < 10; i++)
        {
            if (this.inventorySlots.get(i + 36).getHasStack())
            {
                state = state.with(BookShelf.BOOKS[c], true);
                c++;
            }
        }
        for (; c < 10; c++)
        {
            state = state.with(BookShelf.BOOKS[c], false);
        }

        this.world.setBlockState(pos, state);

    }
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return true;
    }
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
	        this.refreshState();
     }
    
    @Nonnull
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
        return slotIn.inventory != this.book;
    }

    @Nonnull
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
