package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.automation.crystal.FrozenCrystal;
import com.ustctuixue.arcaneart.automation.crystal.MagmaCrystal;
import com.ustctuixue.arcaneart.automation.crystal.SolarCrystal;
import com.ustctuixue.arcaneart.automation.crystal.SolarCrystalTileEntity;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxEmitter;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AutomationRegistry {
    public static final DeferredRegister<Block> BLOCK_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, "arcaneart");

    public static RegistryObject<Block> SOLAR_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("solar_crystal", SolarCrystal::new);
    public static RegistryObject<Block> FROZEN_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("frozen_crystal", FrozenCrystal::new);
    public static RegistryObject<Block> MAGMA_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("magma_crystal", MagmaCrystal::new);
    public static RegistryObject<Block> LUX_EMITTER_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("lux_emitter", LuxEmitter::new);



    public static final DeferredRegister<Item> ITEM_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");

    public static RegistryObject<Item> TEMPERATURE_DEBUG_STICK = ITEM_TYPE_DEFERRED_REGISTER.register("temperature_debug_stick", () -> {return new TemperatureDebugStick();});
    public static RegistryObject<Item> SOLAR_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("solar_crystal", () -> new BlockItem(AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> FROZEN_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("frozen_crystal", () -> new BlockItem(AutomationRegistry.FROZEN_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> MAGMA_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("magma_crystal", () -> new BlockItem(AutomationRegistry.MAGMA_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> LUX_EMITTER_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("lux_emitter", () -> new BlockItem(AutomationRegistry.LUX_EMITTER_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));



    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, "arcaneart");

    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> SOLAR_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("solar_crystal_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> FROZEN_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("frozen_crystal_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.FROZEN_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> MAGMA_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("magma_crystal_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.MAGMA_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> LUX_EMITTER_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("lux_emitter_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.LUX_EMITTER_BLOCK.get()).build(null));

}
