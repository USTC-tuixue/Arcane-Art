package com.ustctuixue.arcaneart.misc;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.misc.BlockRegistry;

import com.ustctuixue.arcaneart.misc.bookshelf.BookShelfBlock;
import com.ustctuixue.arcaneart.misc.bookshelf.BookShelfTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class TileEntityTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER
            = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ArcaneArt.MOD_ID);
    public static RegistryObject<TileEntityType<BookShelfTileEntity>> bookShelfTileEntity
            = TILE_ENTITY_TYPE_DEFERRED_REGISTER
            .register(
                    "block_shelf_tileentity",
                    () -> TileEntityType.Builder.create(
                            BookShelfTileEntity::new,
                            BlockRegistry.BOOKSHELVES.values()
                                    .stream().map(RegistryObject::<BookShelfBlock>get)
                            .collect(Collectors.toSet()).toArray(new BookShelfBlock[0])
                    ).build(null)
            );
}
