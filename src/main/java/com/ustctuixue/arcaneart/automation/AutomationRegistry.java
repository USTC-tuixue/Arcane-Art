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
    public static RegistryObject<Block> collectiveCrystalBlock = BLOCK_TYPE_DEFERRED_REGISTER.register("collective_crystal", () -> {
        return new CollectiveCrystal();
    });

    public static final DeferredRegister<Item> ITEM_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, "arcaneart");
    public static RegistryObject<Item> collectiveCrystalItem = ITEM_TYPE_DEFERRED_REGISTER.register("collective_crystal", () -> {
        return new BlockItem(AutomationRegistry.collectiveCrystalBlock.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    });

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, "arcaneart");
    public static RegistryObject<TileEntityType<CollectiveCrystalTileEntity>> collectiveCrystalTileEntity = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("collective_crystal_tileentity", () -> {
        return TileEntityType.Builder.create(() -> {
            return new CollectiveCrystalTileEntity();
        }, AutomationRegistry.collectiveCrystalBlock.get()).build(null);
    });
}
