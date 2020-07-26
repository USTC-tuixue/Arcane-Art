package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.automation.crystal.SolarCrystal;
import com.ustctuixue.arcaneart.automation.crystal.SolarCrystalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AutomationRegistry {
    public static final DeferredRegister<Block> BLOCK_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, ArcaneArt.MOD_ID);
    public static RegistryObject<Block> SOLAR_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("solar_crystal", SolarCrystal::new);


    public static final DeferredRegister<Item> ITEM_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, ArcaneArt.MOD_ID);
    public static RegistryObject<Item> SOLAR_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("solar_crystal", () -> new BlockItem(AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));


    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ArcaneArt.MOD_ID);
    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> SOLAR_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("collective_crystal_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get()).build(null));

}
