package com.ustctuixue.arcaneart.misc.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "arcaneart");
    public static RegistryObject<BookShelf> bookShelf = BLOCKS.register("book_shelf", () -> {
        return new BookShelf();
    });
}
