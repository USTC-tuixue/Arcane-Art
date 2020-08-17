package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ritual.Ritual;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class RitualModuleRegistries {
    public static final DeferredRegister<Ritual> RITUAL_DEFERRED_REGISTER
            = new DeferredRegister<>(Ritual.REGISTRY, ArcaneArt.MOD_ID);

    public static final RegistryObject<Ritual> RITUAL_RAIN
            = RITUAL_DEFERRED_REGISTER.register("ritual_rain",
                Ritual.builder()
                        .execRitual(new RitualRain())
                        .ingredients(new Ingredient[]{
                                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                                Ingredient.EMPTY, Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.EMPTY,
                                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
                        })
                        .cost(5000)
                        .consumeSpeed(50)
                        ::build
    );

    public void test() {

    }
}
