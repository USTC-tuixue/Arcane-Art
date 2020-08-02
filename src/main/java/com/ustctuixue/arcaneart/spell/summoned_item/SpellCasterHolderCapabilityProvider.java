package com.ustctuixue.arcaneart.spell.summoned_item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpellCasterHolderCapabilityProvider implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> SPELL_CASTER_HOLDER_CAP = null;

    private ItemStackHandler spellCasterSlot = new ItemStackHandler();

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
        if (cap == SPELL_CASTER_HOLDER_CAP)
        {
            return LazyOptional.of(() -> spellCasterSlot).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return spellCasterSlot.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.spellCasterSlot.deserializeNBT(nbt);
    }
}
