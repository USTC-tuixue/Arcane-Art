package com.ustctuixue.arcaneart.api.mp.tile;

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
        static final String MAX_MP = "maxMP";
        static final String CURRENT_MP = "mp";
        static final String IN_LIM = "inputLimit";
        static final String OUT_LIM = "outputLimit";
        @Nullable
        @Override
        public INBT writeNBT(Capability<MPStorage> capability, MPStorage instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putDouble(MAX_MP, instance.maxMP);
            nbt.putDouble(CURRENT_MP, instance.mana);
            return nbt;
        }

        @Override
        public void readNBT(Capability<MPStorage> capability, MPStorage instance, Direction side, INBT nbt)
        {
            CompoundNBT compound = (CompoundNBT) nbt;
            instance.setMana(compound.getDouble(CURRENT_MP));
            instance.setMaxMP(compound.getDouble(MAX_MP));
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        private MPStorage mpstorage = new MPStorage();
        private Storage storage = new Storage();
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == MP_STORAGE_CAP ? LazyOptional.of(()-> mpstorage).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) storage.writeNBT(MP_STORAGE_CAP, mpstorage, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            storage.readNBT(MP_STORAGE_CAP, mpstorage, null, nbt);
        }
    }
}
