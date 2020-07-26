package com.ustctuixue.arcaneart.spell.spell;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListVariableArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.util.EntityList;
import net.minecraft.command.arguments.PotionArgument;
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
    private RelativeEntityListBuilder target;

    @Override
    public double getComplexity(SpellCasterSource source)
    {
        return 0;
    }

    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        double amp = Math.pow(this.effectInstance.getAmplifier() + 1, 2) * effectInstance.getDuration() / 10;
        return EFFECT_COST.get(this.effectInstance.getPotion()) * target.build(source).size() * amp;
    }

    @Override
    public void execute(SpellCasterSource source)
    {

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
        reader.skipWhitespace();
        try{
            target = new EntityListVariableArgument().parse(reader).get();
            reader.skipWhitespace();
            Effect effect = new PotionArgument().parse(reader);
            int amplifier = 0;
            int duration = 10;
            try
            {
                reader.skipWhitespace();
                duration = IntegerArgumentType.integer(0).parse(reader);
                reader.skipWhitespace();
                amplifier = IntegerArgumentType.integer(0).parse(reader);
            }catch (CommandSyntaxException e)
            {
                e.printStackTrace();
            }
            this.effectInstance = new EffectInstance(effect, duration, amplifier);
            return true;
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
