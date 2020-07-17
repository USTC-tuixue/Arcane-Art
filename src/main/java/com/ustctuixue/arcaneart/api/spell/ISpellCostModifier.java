package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.List;

public interface ISpellCostModifier
{
    default double getAddition(double originalCost, LivingEntity entity, World world)
    {
        return 0;
    }

    default double getAmplifier(double originalCost, LivingEntity entity, World world)
    {
        return 1;
    }

    List<ISpellCostModifier> MODIFIERS = Lists.newArrayList();

    static double modifyCost(double originalCost, LivingEntity entity, World world)
    {
        double result = originalCost;
        result += MODIFIERS.stream().mapToDouble(iSpellCostModifier -> iSpellCostModifier.getAddition(originalCost, entity, world)).sum();
        for (ISpellCostModifier modifier : MODIFIERS)
        {
            result *= modifier.getAmplifier(originalCost, entity, world);
        }
        return result;
    }

}
