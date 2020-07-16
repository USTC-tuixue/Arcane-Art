package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import lombok.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

@RequiredArgsConstructor
public class Spell
{
    @Getter @NonNull
    final String spell;

    @Getter @NonNull
    final List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();

    public void playerCastOnHold(IManaBar bar, World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        for (ISpellEffectOnHold effect:
             effectOnHold)
        {
            if (bar.consumeMana(effect.manaCost()))
            {
                effect.onItemRightClick(worldIn, playerIn, handIn);
            }
        }
    }

    public void playerCastOnRelease(IManaBar bar, ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        for (ISpellEffectOnRelease effect :
                effectOnRelease)
        {
            if (bar.consumeMana(effect.manaCost()))
            {
                effect.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
            }
        }
    }


}
