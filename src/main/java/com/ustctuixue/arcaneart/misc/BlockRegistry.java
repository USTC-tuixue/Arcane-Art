package com.ustctuixue.arcaneart.misc;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.misc.bookshelf.BookShelfBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS;

    private static final ResourceLocation[] PLANKS;
    public static final Map<String, RegistryObject<BookShelfBlock>> BOOKSHELVES;

    static {
        BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ArcaneArt.MOD_ID);
        PLANKS = new ResourceLocation[]
                {
                        Blocks.ACACIA_PLANKS.getRegistryName(),
                        Blocks.BIRCH_PLANKS.getRegistryName(),
                        Blocks.DARK_OAK_PLANKS.getRegistryName(),
                        Blocks.JUNGLE_PLANKS.getRegistryName(),
                        Blocks.OAK_PLANKS.getRegistryName(),
                        Blocks.SPRUCE_PLANKS.getRegistryName()
                };
        BOOKSHELVES = Maps.newHashMap();
        for (ResourceLocation plank : PLANKS)
        {
            assert plank != null;
            String woodName = plank.getPath().replace("_planks", "");
            BOOKSHELVES.put(
                    woodName,
                    BLOCKS.register(woodName + "_bookshelf", BookShelfBlock::new)
            );
        }
    }

}
