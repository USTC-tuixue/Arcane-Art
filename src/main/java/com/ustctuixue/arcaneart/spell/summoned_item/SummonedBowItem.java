package com.ustctuixue.arcaneart.spell.summoned_item;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class SummonedBowItem extends BowItem
{
    public SummonedBowItem(Properties builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new SpellCasterHolderCapabilityProvider();
    }
}
