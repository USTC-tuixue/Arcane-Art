package com.ustctuixue.arcaneart.ritual.device;

import com.ustctuixue.arcaneart.ritual.RitualRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DingTileEntity extends TileEntity {

    public DingTileEntity() {
        super(RitualRegistries.dingTileEntity.get());
    }

    protected ItemStackHandler itemStackHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            super.onContentsChanged(slot);
        }
    };

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(!Direction.DOWN.equals(side) && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(()->itemStackHandler).cast();
        }
        return super.getCapability(cap, side);
    }

    public ItemStack getItemStored() {
        return itemStackHandler.getStackInSlot(0);
    }


    @Override
    public void read(CompoundNBT compound) {
        itemStackHandler.deserializeNBT(compound.getCompound("store"));
        if(this.world != null && !this.world.isRemote) {
            this.world.setBlockState(this.getPos(), this.getBlockState().with(DingBlock.LOCK, false));
        }
        super.read(compound);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("store", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void onChunkUnloaded() {
        if(this.world != null && !this.world.isRemote) {
            this.world.setBlockState(this.getPos(), this.getBlockState().with(DingBlock.LOCK, false));
        }
    }
}
