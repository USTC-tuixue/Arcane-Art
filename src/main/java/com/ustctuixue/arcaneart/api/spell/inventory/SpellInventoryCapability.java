package com.ustctuixue.arcaneart.api.spell.inventory;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SpellInventoryCapability {
    @CapabilityInject(ISpellInventory.class)
    public static Capability<ISpellInventory> SPELL_INVENTORY_CAPABILITY = null;
}
