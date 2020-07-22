package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AutomationRegistry {
    public static final DeferredRegister<Block> BLOCK_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, "arcaneart");

    public static RegistryObject<Block> COLLECTIVE_CRYSTAL_BLOCK = BLOCK_TYPE_DEFERRED_REGISTER.register("collective_crystal", SolarCrystal::new);

    public static final DeferredRegister<Item> ITEM_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");
    public static RegistryObject<Item> COLLECTIVE_CRYSTAL_ITEM = ITEM_TYPE_DEFERRED_REGISTER.register("collective_crystal", () -> new BlockItem(AutomationRegistry.COLLECTIVE_CRYSTAL_BLOCK.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP)));

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, "arcaneart");
//    public static RegistryObject<TileEntityType<AbstractCollectiveCrystalTileEntity>> COLLECTIVE_CRYSTAL_TILEENTITY = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("collective_crystal_tileentity", () -> TileEntityType.Builder.create(AbstractCollectiveCrystalTileEntity::new, AutomationRegistry.COLLECTIVE_CRYSTAL_BLOCK.get()).build(null));

}
