package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.mojang.datafixers.types.templates.Tag;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ritual.Ritual;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class RitualModuleRegistries {
    public static final DeferredRegister<Ritual> RITUAL_DEFERRED_REGISTER
            = new DeferredRegister<>(Ritual.REGISTRY, ArcaneArt.MOD_ID);
    public static final DeferredRegister<Item> ITEMS
            = new DeferredRegister<>(ForgeRegistries.ITEMS, ArcaneArt.MOD_ID);

    public static final RegistryObject<Ritual> RITUAL_RAIN
            = RITUAL_DEFERRED_REGISTER.register("ritual_rain",
                Ritual.builder()
                        .execRitual(RitualRain::new)
                        .ingredients(new Ingredient[]{
                                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                                Ingredient.EMPTY, Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.EMPTY,
                                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
                        })
                        .cost(5000)
                        .consumeSpeed(50)
                        ::build
    );

    public static final RegistryObject<Ritual> RITUAL_CLEAR
            = RITUAL_DEFERRED_REGISTER.register("ritual_clear",
            Ritual.builder()
                    .execRitual(RitualClear::new)
                    .ingredients(new Ingredient[]{
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.fromItems(Items.LAVA_BUCKET), Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
                    })
                    .cost(5000)
                    .consumeSpeed(50)
                    ::build
    );

    public static final RegistryObject<Ritual> RITUAL_THUNDER
            = RITUAL_DEFERRED_REGISTER.register("ritual_thunder",
            Ritual.builder()
                    .execRitual(RitualClear::new)
                    .ingredients(new Ingredient[]{
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.fromItems(Items.TRIDENT), Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
                    })
                    .cost(6000)
                    .consumeSpeed(50)
                    ::build
    );

    public static final RegistryObject<Ritual> RITUAL_APPEND_SPELL
            = RITUAL_DEFERRED_REGISTER.register("ritual_append_spell",
            Ritual.builder()
                    .execRitual(RitualClear::new)
                    .ingredients(new Ingredient[]{
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.fromItems(Items.GOLDEN_AXE), Ingredient.EMPTY,
                            Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
                    })
                    .cost(6000)
                    .consumeSpeed(50)
                    ::build
    );

    {
        Style NUMBER_STYLE = new Style().setColor(TextFormatting.YELLOW);
        for(Ritual i : Ritual.REGISTRY) {
            String name = Objects.requireNonNull(i.getRegistryName()).getPath();
            ITEMS.register("fake_item_"+ name, ()->new FakeRitualItem() {
                @Override
                public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
                    tooltip.add(new TranslationTextComponent("info.arcaneart.ritual.total_mana",
                            new StringTextComponent(String.valueOf(i.getCost())).setStyle(NUMBER_STYLE)));
                    tooltip.add(new TranslationTextComponent("info.arcaneart.ritual.mana_per_tick",
                            new StringTextComponent(String.valueOf(i.getConsumeSpeed())).setStyle(NUMBER_STYLE)));
                    tooltip.add(new TranslationTextComponent("description.arcaneart."+name));

                    super.addInformation(stack, worldIn, tooltip, flagIn);
                }
            });
        }
    }
}
