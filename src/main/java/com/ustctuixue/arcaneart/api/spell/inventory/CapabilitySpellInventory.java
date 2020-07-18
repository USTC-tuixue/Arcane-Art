package com.ustctuixue.arcaneart.api.spell.inventory;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilitySpellInventory {

	public class Provider implements ICapabilityProvider {

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	public static final ICapabilityProvider Provider = null;
	public CapabilitySpellInventory(){
		
	}

}
