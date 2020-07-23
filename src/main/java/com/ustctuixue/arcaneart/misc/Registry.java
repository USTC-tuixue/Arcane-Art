package com.ustctuixue.arcaneart.misc;

import com.ustctuixue.arcaneart.misc.block.BlockRegistry;
import com.ustctuixue.arcaneart.misc.item.ItemRegistry;
import com.ustctuixue.arcaneart.misc.tileentity.TileEntityTypeRegistry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
	public static void register(IEventBus bus) {
		ContainerTypeRegistry.CONTAINERS.register(bus);
		BlockRegistry.BLOCKS.register(bus);
		ItemRegistry.ITEMS.register(bus);
		TileEntityTypeRegistry.TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(bus);
	}
}
