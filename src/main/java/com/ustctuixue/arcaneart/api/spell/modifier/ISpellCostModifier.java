package com.ustctuixue.arcaneart.api.spell.modifier;

import com.google.common.collect.Lists;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("unused")
public interface ISpellCostModifier
{
    default double getAddition(double originalCost, SpellCasterSource source)
    {
        return 0;
    }

    default double getAmplifier(double originalCost, SpellCasterSource source)
    {
        return 1;
    }

    List<ISpellCostModifier> MODIFIERS = Lists.newArrayList();

    static double modifyCost(double originalCost, SpellCasterSource source)
    {
        double result = originalCost;
        result += MODIFIERS.stream().mapToDouble(iSpellCostModifier -> iSpellCostModifier.getAddition(originalCost, source)).sum();
        for (ISpellCostModifier modifier : MODIFIERS)
        {
            result *= modifier.getAmplifier(originalCost, source);
        }
        return result;
    }

}
