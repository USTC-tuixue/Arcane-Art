package com.ustctuixue.arcaneart.misc.tileentity;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.misc.block.BlockRegistry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER
            = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ArcaneArt.MOD_ID);
    public static RegistryObject<TileEntityType<BookShelfTileEntity>> bookShelfTileEntity
            = TILE_ENTITY_TYPE_DEFERRED_REGISTER
            .register(
                    "block_shelf_tileentity",
                    () -> TileEntityType.Builder.create(
                            BookShelfTileEntity::new,
                            BlockRegistry.bookShelf.get()
                    ).build(null)
            );
}
