package com.ustctuixue.arcaneart.misc;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.misc.BlockRegistry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ItemRegistry {
	  public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");
	  public static final Map<String, RegistryObject<BlockItem>> BOOKSHELVES_ITEM = Maps.newHashMap();

	  static
	  {
	  	BlockRegistry.BOOKSHELVES.forEach(((s, bookShelfBlockRegistryObject) ->
		{
	  		BOOKSHELVES_ITEM.put(s, ITEMS.register(s + "_bookshelf", () ->
					new BlockItem(
							bookShelfBlockRegistryObject.get(),
							new Item.Properties()
									.group(ArcaneArt.ARCANE_ART_ITEM_GROUP)
					)
			));
		}));
	  }
}
