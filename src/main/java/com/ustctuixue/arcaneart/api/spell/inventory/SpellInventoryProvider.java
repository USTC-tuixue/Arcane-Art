package com.ustctuixue.arcaneart.api.spell.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class SpellInventoryProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT>
{
    private ISpellInventory spellInventoryCapability;

    @Override
    public CompoundNBT serializeNBT()
    {
        return getOrCreateCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        getOrCreateCapability().deserializeNBT(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        return cap == SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY ?
				LazyOptional.of(this::getOrCreateCapability).cast() : LazyOptional.empty();
    }

    @Nonnull
    private ISpellInventory getOrCreateCapability()
    {
        if (spellInventoryCapability == null)
        {
            this.spellInventoryCapability = new SpellInventory();
        }
        return this.spellInventoryCapability;
    }
}
