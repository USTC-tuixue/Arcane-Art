package com.ustctuixue.arcaneart.misc.tileentity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class BookShelfTileEntity extends TileEntity implements INamedContainerProvider {
	private Inventory inventory = new Inventory(10);

    public BookShelfTileEntity() {
        super(TileEntityTypeRegistry.bookShelfTileEntity.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("title.arcaneart.bookshelf");
    }

    @Nullable
    @Override
    public Container createMenu(int sycID, PlayerInventory inv, PlayerEntity player) {
        return new BookShelfContainer(sycID, inv, this.pos, this.world);
    }

    @Override
    public void read(CompoundNBT compound) {
    	this.inventory = new Inventory(10);
		ListNBT list = compound.getList("item", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 10; i++) {
			this.inventory.setInventorySlotContents(i, ItemStack.read(list.getCompound(i)));
		}
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStack itemStack = this.inventory.getStackInSlot(0).copy();
        ListNBT list=new ListNBT();
        for(int i=0;i<10;i++) {
        	CompoundNBT compoundnbt=new CompoundNBT();
			this.inventory.getStackInSlot(i).write(compoundnbt);
			list.add(compoundnbt);
        }
        compound.put("item", list);
        return super.write(compound);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
