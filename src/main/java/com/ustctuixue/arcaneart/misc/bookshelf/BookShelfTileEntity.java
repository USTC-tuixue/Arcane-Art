package com.ustctuixue.arcaneart.misc.bookshelf;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ustctuixue.arcaneart.misc.TileEntityTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class BookShelfTileEntity extends TileEntity implements INamedContainerProvider {
	private Inventory inventory = new Inventory(10);

    public BookShelfTileEntity() {
        super(TileEntityTypeRegistry.bookShelfTileEntity.get());
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("title.arcaneart.bookshelf");
    }

    @Nullable
    @Override
    public Container createMenu(int sycID, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player) {
        assert this.world != null;
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

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
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
