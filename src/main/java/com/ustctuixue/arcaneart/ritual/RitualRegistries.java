package com.ustctuixue.arcaneart.ritual;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.ritual.device.DingBlock;
import com.ustctuixue.arcaneart.ritual.device.DingTileEntity;
import com.ustctuixue.arcaneart.ritual.device.RitualTableBlock;
import com.ustctuixue.arcaneart.ritual.device.RitualTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualRegistries {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ArcaneArt.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ArcaneArt.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ArcaneArt.MOD_ID);

    public static RegistryObject<Block> dingBlockCircle = BLOCKS.register("ding_circle", ()->{
        return new DingBlock(DingBlock.EnumShape.CIRCLE);
    });
    public static RegistryObject<Block> dingBlockSquare = BLOCKS.register("ding_square", ()->{
        return new DingBlock(DingBlock.EnumShape.SQUARE);
    });
    public static RegistryObject<Block> dingBlockCenter = BLOCKS.register("ding_center", ()->{
        return new DingBlock(DingBlock.EnumShape.CENTER);
    });
    public static RegistryObject<Block> ritualTableBlock = BLOCKS.register("ritual_table", RitualTableBlock::new);

    public static RegistryObject<Item> dingItemCircle = ITEMS.register("item_ding_circle", ()->{
        return new BlockItem(dingBlockCircle.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    });
    public static RegistryObject<Item> dingItemSquare = ITEMS.register("item_ding_square", ()->{
        return new BlockItem(dingBlockSquare.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    });
    public static RegistryObject<Item> dingItemCenter = ITEMS.register("item_ding_center", ()->{
        return new BlockItem(dingBlockCenter.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    });
    public static RegistryObject<Item> tableItem = ITEMS.register("item_ritual_table", ()->{
        return new BlockItem(ritualTableBlock.get(), new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    });

    public static RegistryObject<TileEntityType<? extends DingTileEntity>> dingTileEntity
            = TILE_ENTITIES.register("ding_tile_entity", ()->{
        return TileEntityType.Builder.create(DingTileEntity::new,
                dingBlockCircle.get(),
                dingBlockSquare.get(),
                dingBlockCenter.get()).build(null);
    });

    public static RegistryObject<TileEntityType<RitualTableTileEntity>> ritualTableTileEntity
            = TILE_ENTITIES.register("ritual_table_tile_entity", ()->{
        return TileEntityType.Builder.create(RitualTableTileEntity::new, ritualTableBlock.get()).build(null);
    });
}
