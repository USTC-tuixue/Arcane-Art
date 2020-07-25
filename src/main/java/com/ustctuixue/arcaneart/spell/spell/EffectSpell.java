package com.ustctuixue.arcaneart.spell.spell;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.util.EntityList;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.Map;

public class EffectSpell implements ISpell
{
    public static Map<Effect, Double> EFFECT_COST = ImmutableMap.of(
            Effects.NIGHT_VISION, 100D
    );

    private EffectInstance effectInstance;
    private EntityList target;

    @Override
    public double getComplexity(SpellCasterSource source)
    {
        return 0;
    }

    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        return 0;
    }

    @Override
    public void execute(SpellCasterSource source){
    }

    /**
     * parse spell into self
     *
     * @param reader reader
     * @return true if successfully parsed
     */
    @Override
    public boolean parse(StringReader reader)
    {
        return false;
    }
}
