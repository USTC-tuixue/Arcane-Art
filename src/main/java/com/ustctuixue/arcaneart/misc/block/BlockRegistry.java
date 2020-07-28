package com.ustctuixue.arcaneart.misc.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "arcaneart");
    public static RegistryObject<BookShelf> bookShelfAcacia = BLOCKS.register("acacia_book_shelf", () -> {
        return new AcaciaBookShelf();
    });
    public static RegistryObject<BookShelf> bookShelfBirch = BLOCKS.register("birch_book_shelf", () -> {
        return new BirchBookShelf();
    });
    public static RegistryObject<BookShelf> bookShelfDarkOak = BLOCKS.register("dark_oak_book_shelf", () -> {
        return new DarkOakBookShelf();
    });
    public static RegistryObject<BookShelf> bookShelfJungle = BLOCKS.register("jungle_book_shelf", () -> {
        return new JungleBookShelf();
    });
    public static RegistryObject<BookShelf> bookShelfOak = BLOCKS.register("oak_book_shelf", () -> {
        return new OakBookShelf();
    });
    public static RegistryObject<BookShelf> bookShelfSpruce = BLOCKS.register("spruce_book_shelf", () -> {
        return new SpruceBookShelf();
    });
}
