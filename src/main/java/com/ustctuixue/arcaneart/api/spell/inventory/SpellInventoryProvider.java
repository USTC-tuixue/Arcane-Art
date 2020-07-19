package com.ustctuixue.arcaneart.api.spell.inventory;


import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SpellInventoryProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
	private ISpellInventory spellInventoryCapability=null;
	@Override
	public CompoundNBT serializeNBT() {
		return getOrCreateCapability().serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		getOrCreateCapability().deserializeNBT(nbt);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY ? LazyOptional.of(() -> {
            return this.getOrCreateCapability();
        }).cast() : LazyOptional.empty();
	}

	private ISpellInventory getOrCreateCapability() {        
		if (spellInventoryCapability == null) {
        this.spellInventoryCapability = new SpellInventory();
    }
    return this.spellInventoryCapability;
	}
}