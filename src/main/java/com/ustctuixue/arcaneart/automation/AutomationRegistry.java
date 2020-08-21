package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.automation.crystal.*;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxEmitter;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxEmitterTileentity;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxReflector;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("WeakerAccess")
public class AutomationRegistry {

    public static final DeferredRegister<Block> BLOCK_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, "arcaneart");

    public static RegistryObject<Block> SOLAR_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("solar_crystal", SolarCrystal::new);
    public static RegistryObject<Block> FROZEN_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("frozen_crystal", FrozenCrystal::new);
    public static RegistryObject<Block> MAGMA_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("magma_crystal", MagmaCrystal::new);
    public static RegistryObject<Block> VOID_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("void_crystal", VoidCrystal::new);
    public static RegistryObject<Block> LUX_EMITTER_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("lux_emitter", LuxEmitter::new);
    public static RegistryObject<Block> LUX_REFLECTOR_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("lux_reflector", LuxReflector::new);
    public static RegistryObject<Block> LUX_SPLITTER_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("lux_splitter", LuxReflector::new);
    public static RegistryObject<Block> MANA_FLOWER_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("mana_flower", ManaFlower::new);



    public static final DeferredRegister<Item> ITEM_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");

    public static RegistryObject<Item> LUX_WAND = ITEM_TYPE_DEFERRED_REGISTER.register("lux_wand", LuxWand::new);
    public static RegistryObject<Item> SOLAR_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("solar_crystal", () -> new BlockItem(AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> FROZEN_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("frozen_crystal", () -> new BlockItem(AutomationRegistry.FROZEN_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> MAGMA_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("magma_crystal", () -> new BlockItem(AutomationRegistry.MAGMA_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> VOID_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("void_crystal", () -> new BlockItem(AutomationRegistry.VOID_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> LUX_EMITTER_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("lux_emitter", () -> new BlockItem(AutomationRegistry.LUX_EMITTER_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> LUX_REFLECTOR_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("lux_reflector", () -> new BlockItem(AutomationRegistry.LUX_REFLECTOR_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> LUX_SPLITTER_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("lux_splitter", () -> new BlockItem(AutomationRegistry.LUX_SPLITTER_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));
    public static RegistryObject<Item> MANA_FLOWER_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("mana_flower", () -> new BlockItem(AutomationRegistry.MANA_FLOWER_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));



    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, "arcaneart");

    public static RegistryObject<TileEntityType<SolarCrystalTileEntity>> SOLAR_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("solar_crystal_tileentity", () -> TileEntityType.Builder.create(SolarCrystalTileEntity::new, AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<FrozenCrystalTileEntity>> FROZEN_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("frozen_crystal_tileentity", () -> TileEntityType.Builder.create(FrozenCrystalTileEntity::new, AutomationRegistry.FROZEN_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<MagmaCrystalTileEntity>> MAGMA_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("magma_crystal_tileentity", () -> TileEntityType.Builder.create(MagmaCrystalTileEntity::new, AutomationRegistry.MAGMA_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<VoidCrystalTileEntity>> VOID_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("void_crystal_tileentity", () -> TileEntityType.Builder.create(VoidCrystalTileEntity::new, AutomationRegistry.VOID_CRYSTAL_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<LuxEmitterTileentity>> LUX_EMITTER_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("lux_emitter_tileentity", () -> TileEntityType.Builder.create(LuxEmitterTileentity::new, AutomationRegistry.LUX_EMITTER_BLOCK.get()).build(null));
    public static RegistryObject<TileEntityType<ManaFlowerTileentity>> MANA_FLOWER_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("mana_flower_tileentity", () -> TileEntityType.Builder.create(ManaFlowerTileentity::new, AutomationRegistry.MANA_FLOWER_BLOCK.get()).build(null));

}
