package com.ustctuixue.arcaneart.ritual.device;

import com.ustctuixue.arcaneart.ritual.RitualRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class DingTileEntity extends TileEntity {

    public DingTileEntity() {
        super(RitualRegistries.dingTileEntity.get());
    }
/*
    public static class DingCircleTileEntity extends DingTileEntity {
        public DingCircleTileEntity() {
            super(RitualRegistry.dingCircleTileEntity.get());
        }
    }
    public static class DingSquareTileEntity extends DingTileEntity {
        public DingSquareTileEntity() {
            super(RitualRegistry.dingSquareTileEntity.get());
        }
    }
    public static class DingCenterTileEntity extends DingTileEntity {
        public DingCenterTileEntity() {
            super(RitualRegistry.dingCenterTileEntity.get());
        }
    }
*/
    protected ItemStackHandler itemStackHandler = new ItemStackHandler();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if(side != Direction.DOWN && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(()->itemStackHandler).cast();
        }
        return super.getCapability(cap, side);
    }

    public ItemStack getItemStored() {
        return itemStackHandler.getStackInSlot(0);
    }
}
