package com.ustctuixue.arcaneart.api.mp.mpstorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityMPStorage
{
    @CapabilityInject(MPStorage.class)
    public static Capability<MPStorage> MP_STORAGE_CAP = null;

    public static class Storage implements Capability.IStorage<MPStorage>
    {
        static final String MAX_MP = "max_mana";
        static final String CURRENT_MP = "mana";

        @Nullable
        @Override
        public INBT writeNBT(Capability<MPStorage> capability, MPStorage instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putDouble(MAX_MP, instance.maxMana);
            nbt.putDouble(CURRENT_MP, instance.mana);
            return nbt;
        }

        @Override
        public void readNBT(Capability<MPStorage> capability, MPStorage instance, Direction side, INBT nbt)
        {
            CompoundNBT compound = (CompoundNBT) nbt;
            instance.setMana(compound.getDouble(CURRENT_MP));
            instance.setMaxMana(compound.getDouble(MAX_MP));
        }
    }

}
