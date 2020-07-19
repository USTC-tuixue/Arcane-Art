package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitySpell
{
    @CapabilityInject(Spell.class)
    public static Capability<Spell> SPELL_CASTER_CAP = null;

    public static class Storage implements Capability.IStorage<Spell>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<Spell> capability, Spell instance, Direction side)
        {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<Spell> capability, Spell instance, Direction side, INBT nbt)
        {
            instance.deserializeNBT((ListNBT) nbt);
        }
    }

    public static class Provider implements ICapabilitySerializable<ListNBT>
    {
        Spell spell;

        /**
         * Retrieves the Optional handler for the capability requested on the specific side.
         * The return value <strong>CAN</strong> be the same for multiple faces.
         * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
         * be notified if the requested capability get lost.
         *
         * @param cap
         * @param side
         * @return The requested an optional holding the requested capability.
         */
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CASTER_CAP ? LazyOptional.of(()->spell).cast() : LazyOptional.empty();
        }

        @Override
        public ListNBT serializeNBT()
        {
            return spell.serializeNBT();
        }

        @Override
        public void deserializeNBT(ListNBT nbt)
        {
            spell.deserializeNBT(nbt);
        }
    }

}
