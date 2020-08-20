package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListVariableArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.util.EntityList;
import com.ustctuixue.arcaneart.spell.SpellModuleConfig;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import org.apache.logging.log4j.LogManager;

public class EffectSpell implements ISpell
{
    private EffectInstance effectInstance;
    private RelativeEntityListBuilder target;

    @Override
    public double getComplexityBase(SpellCasterSource source)
    {
        double amp = SpellModuleConfig.SpellProperty.EffectSpell
                .getComplexityAmplifier(
                        this.effectInstance.getAmplifier(),
                        this.effectInstance.getDuration(),
                        this.target.build(source).size() 
                );
        return SpellModuleConfig.SpellProperty.EffectSpell.getEffectSettings()
                .get(this.effectInstance.getPotion()).getBasicComplexity()
                * amp;
    }

    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        double amp = SpellModuleConfig.SpellProperty.EffectSpell
                .getManaCostAmplifier(
                        this.effectInstance.getAmplifier(),
                        this.effectInstance.getDuration(),
                        this.target.build(source).size()
                );
        return SpellModuleConfig.SpellProperty.EffectSpell.getEffectSettings()
                .get(this.effectInstance.getPotion()).getBasicCost()
                * amp;
    }

    @Override
    public void execute(SpellCasterSource source)
    {
        LogManager.getLogger(this.getClass()).debug("Effect Instance: " + this.effectInstance);
        if (this.effectInstance != null)
        {
            EntityList targetList = this.target.build(source);

            for (Entity entity : targetList)
            {
                if (entity instanceof LivingEntity)
                {
                    LivingEntity entityLiving = (LivingEntity) entity;
                    LogManager.getLogger(this.getClass()).debug("Target:" + entity);
                    if (effectInstance.getPotion().isInstant()) {
                        effectInstance.getPotion().affectEntity(source.getEntity(), source.getEntity(), entityLiving, effectInstance.getAmplifier(), 1.0D);
                    } else {
                        entityLiving.addPotionEffect(new EffectInstance(effectInstance));
                    }
                }
            }
        }
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
            if (reader.canRead())
            {
                try
                {
                    reader.skipWhitespace();
                    duration = IntegerArgumentType.integer(0).parse(reader);
                    reader.skipWhitespace();
                    amplifier = IntegerArgumentType.integer(0).parse(reader);
                } catch (CommandSyntaxException e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            this.effectInstance = new EffectInstance(effect, duration, amplifier);
            ArcaneArt.LOGGER.info(this.effectInstance.toString());
            return true;
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double guessManaCost(SpellCasterSource source)
    {
        double amp = SpellModuleConfig.SpellProperty.EffectSpell
                .getManaCostAmplifier(
                        this.effectInstance.getAmplifier(),
                        this.effectInstance.getDuration(),
                        this.target.getLimit()
                );
        return SpellModuleConfig.SpellProperty.EffectSpell.getEffectSettings()
                .get(this.effectInstance.getPotion()).getBasicCost()
                * amp;
    }
}
