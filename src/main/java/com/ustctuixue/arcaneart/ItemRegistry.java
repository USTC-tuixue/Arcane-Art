package com.ustctuixue.arcaneart;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");
	/*public static RegistryObject<Item> obsidianIngot = ITEMS.register("xxxx_xxxx", () -> {
		return xxxx;
	});*/
}