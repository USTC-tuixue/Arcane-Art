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
	  public static RegistryObject<Item> bookShelfAcacia = ITEMS.register("acacia_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfAcacia.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
	  public static RegistryObject<Item> bookShelfBirch = ITEMS.register("birch_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfBirch.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
	  public static RegistryObject<Item> bookShelfDarkOak = ITEMS.register("dark_oak_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfDarkOak.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
	  public static RegistryObject<Item> bookShelfJungle = ITEMS.register("jungle_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfJungle.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
	  public static RegistryObject<Item> bookShelfOak = ITEMS.register("oak_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfOak.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
	  public static RegistryObject<Item> bookShelfSpruce = ITEMS.register("spruce_book_shelf", () -> {
		  return new BlockItem(BlockRegistry.bookShelfSpruce.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
		});
}
