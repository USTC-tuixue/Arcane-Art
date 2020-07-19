package com.ustctuixue.arcaneart.api;

import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TestObjects
{
    private static final DeferredRegister<Item> ITEM = new DeferredRegister<>(ForgeRegistries.ITEMS, ArcaneArtAPI.MOD_ID);

    public static final RegistryObject<Item> TEST_CASTER = ITEM.register("test_caster", () ->
            new ItemSpellCaster(
                    new Item.Properties().setNoRepair().maxStackSize(1)
            ));

    public static void register()
    {
        ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
