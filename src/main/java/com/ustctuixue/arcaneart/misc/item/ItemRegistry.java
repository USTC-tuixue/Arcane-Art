package com.ustctuixue.arcaneart.misc.item;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.misc.block.BlockRegistry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	  public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");
	  public static RegistryObject<Item> bookShelf = ITEMS.register("book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelf.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
}
