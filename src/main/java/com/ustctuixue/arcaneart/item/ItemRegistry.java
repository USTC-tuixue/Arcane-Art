package com.ustctuixue.arcaneart.item;

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
	public static RegistryObject<Item> diamond_staff = ITEMS.register("diamond_staff", () -> {
		return new diamondStaff();
	});
}