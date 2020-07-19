package com.ustctuixue.arcaneart.api.spell.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpellEffectOnRelease extends ISpellCost
{
    void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft);
    int chargeTick();
}
