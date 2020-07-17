package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import lombok.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.MODULE)
public class Spell
{
    @NonNull @Getter
    private String name;

    @NonNull
    private final List<ISpellEffectOnHold> effectOnHold;

    @NonNull
    private final List<ISpellEffectOnImpact> effectOnImpact;

    @NonNull
    final List<ISpellEffectOnRelease> effectOnRelease;

    @Getter @NonNull
    final double costOnHold;

    @Getter @NonNull
    final double costOnRelease;

    @Getter @NonNull
    final int chargeTick;

    public boolean playerCastOnHold(IManaBar bar, World worldIn, LivingEntity entityLiving, ItemStack stack, int time)
    {

        boolean f = bar.consumeMana(ISpellCostModifier.modifyCost(costOnHold, entityLiving, worldIn));
        if (f)
        {
            effectOnHold.forEach(effect ->
                    effect.onUsingTick(worldIn, entityLiving, stack, time)
            );
        }
        return f;
    }

    public boolean playerCastOnRelease(IManaBar bar, ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {

        boolean f = bar.consumeMana(ISpellCostModifier.modifyCost(costOnRelease, entityLiving, worldIn));
        if (f && 72000 - timeLeft > chargeTick)
        {
            effectOnRelease.forEach(effect ->
                    effect.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft)
            );
        }
        return f;
    }
}
