package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListVariableArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.spell.SpellConfig;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class EffectSpell implements ISpell
{

    private EffectInstance effectInstance;
    private RelativeEntityListBuilder target;

    @Override
    public double getComplexity(SpellCasterSource source)
    {
        double amp = SpellConfig.SpellProperty.EffectSpell
                .getComplexityAmplifier(
                        this.effectInstance.getAmplifier(),
                        this.effectInstance.getDuration(),
                        this.target.build(source).size()
                );
        return SpellConfig.SpellProperty.EffectSpell.getEffectSettings()
                .get(this.effectInstance.getPotion()).getBasicComplexity()
                * amp;
    }

    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        double amp = SpellConfig.SpellProperty.EffectSpell
                .getManaCostAmplifier(
                        this.effectInstance.getAmplifier(),
                        this.effectInstance.getDuration(),
                        this.target.build(source).size()
                );
        return SpellConfig.SpellProperty.EffectSpell.getEffectSettings()
                .get(this.effectInstance.getPotion()).getBasicCost()
                * target.build(source).size() * amp;
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
