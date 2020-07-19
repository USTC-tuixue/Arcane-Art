package com.ustctuixue.arcaneart.api.spell.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ISpellEffectOnHold extends ISpellCost
{
    void onUsingTick(World worldIn, LivingEntity playerIn, ItemStack stack, int time);
}
