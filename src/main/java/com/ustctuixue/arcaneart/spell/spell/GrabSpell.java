package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListVariableArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionVariableArgument;
import com.ustctuixue.arcaneart.api.util.EntityList;
import com.ustctuixue.arcaneart.api.util.Vec3dList;
import com.ustctuixue.arcaneart.spell.SpellModuleConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class GrabSpell implements ISpell
{
    private Variable<RelativeEntityListBuilder> target;
    private Variable<DirectionBuilder> motion;
    private double distance;

    /**
     * Get spell complexity according to spell source
     *
     * @param source spell source
     * @return complexity
     */
    @Override
    public double getComplexityBase(SpellCasterSource source)
    {
        return SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getBaseComplexity()
                *
                SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getComplexityAmp(
                        target.get().build(source).size(),
                        distance
                );
    }

    /**
     * @param source spell source
     * @return mana base
     */
    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        return SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getManaCostAmp(
                target.get().build(source).size(),
                distance
        ) * SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getBaseManaCost();
    }

    /**
     * Execute spell
     *
     * @param source spell source
     */
    @Override
    public void execute(SpellCasterSource source)
    {
        EntityList targets = target.get().build(source);
        Vec3dList dir = motion.get().build(source);
        if (dir.size() == targets.size())
        {
            for (int i = 0; i < targets.size(); ++i)
            {
                Vec3d vec = dir.get(i).scale(distance);
                Entity e = targets.get(i);
                targets.get(i).setVelocity(0, 0, 0);
                e.setRawPosition(e.getPosX() + vec.x, e.getPosY() + vec.y, e.getPosZ() + vec.z);
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
        try{
            reader.skipWhitespace();
            target = new EntityListVariableArgument().parse(reader);
            reader.skipWhitespace();
            motion = new DirectionVariableArgument().parse(reader);
            try
            {
                reader.skipWhitespace();
                distance = reader.readDouble();
            }catch (CommandSyntaxException e)
            {
                distance = 1;
            }
        } catch (CommandSyntaxException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public double guessManaCost(SpellCasterSource source)
    {
        return SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getManaCostAmp(
                target.get().getLimit(),
                distance
        ) * SpellModuleConfig.SpellProperty.GRAB_SPELL_CONFIG.getBaseManaCost();
    }
}
