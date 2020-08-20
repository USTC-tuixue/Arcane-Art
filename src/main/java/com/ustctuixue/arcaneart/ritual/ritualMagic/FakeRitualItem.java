package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FakeRitualItem extends Item {
    public FakeRitualItem() {
        super(new Properties().
                    group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("info.arcaneart.ritual.fake_item_description").setStyle(
                new Style().setColor(TextFormatting.DARK_RED)
        ));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
