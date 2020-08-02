package com.ustctuixue.arcaneart.spell.summoned_item;

import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class SummonedHoeItem extends HoeItem
{
    public SummonedHoeItem(IItemTier tier, float attackSpeedIn, Properties builder)
    {
        super(tier, attackSpeedIn, builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new SpellCasterHolderCapabilityProvider();
    }
}
