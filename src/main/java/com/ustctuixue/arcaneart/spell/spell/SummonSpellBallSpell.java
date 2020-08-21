package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.entityspellball.EntitySpellBall;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellContainer;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause.TowardsClause;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionBuilder;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import com.ustctuixue.arcaneart.spell.SpellModuleRegistries;

public class SummonSpellBallSpell implements ISpell
{
    private TranslatedSpell spell;
    private Variable<DirectionBuilder> vec;
    private double speed;
    private final ITranslatedSpellProvider provider = new ITranslatedSpellProvider.Impl();
    private int maxLoop;

    /**
     * Get spell complexity according to spell source
     *
     * @param source spell source
     * @return complexity
     */
    @Override
    public double getComplexityBase(SpellCasterSource source)
    {
        SpellContainer compiled = this.provider.getCompiled(source);
        double cost = compiled.preProcess.stream().mapToDouble(iSpell -> iSpell.getComplexityBase(source)).sum();
        cost += compiled.onHold.stream().mapToDouble(iSpell -> iSpell.getComplexityBase(source)).sum() * this.maxLoop;
        cost += compiled.onRelease.stream().mapToDouble(iSpell -> iSpell.getComplexityBase(source)).sum();
        return cost;
    }

    /**
     * @param source spell source
     * @return mana base
     */
    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        SpellContainer compiled = this.provider.getCompiled(source);
        double cost = compiled.preProcess.stream().mapToDouble(iSpell -> iSpell.getManaCostBase(source)).sum();
        cost += compiled.onHold.stream().mapToDouble(iSpell -> iSpell.getManaCostBase(source)).sum() * this.maxLoop;
        cost += compiled.onRelease.stream().mapToDouble(iSpell -> iSpell.getManaCostBase(source)).sum();
        cost += 10;
        return cost;
    }

    /**
     * Execute spell
     *
     * @param source spell source
     */
    @Override
    public void execute(SpellCasterSource source)
    {
        EntitySpellBall spellBall;

        if (this.vec == null)
        {
            spellBall = new EntitySpellBall.Builder(source.getWorld())
                    .setFullMP(this.getManaCostBase(source))
                    .build();
        }
        else
        {
            spellBall = new EntitySpellBall.Builder(source.getWorld())
                    .motion(source.getVariable(this.vec.getName(), this.vec.getType()).build(source).next().scale(this.speed))
                    .setFullMP(this.getManaCostBase(source))
                    .build();
        }
        spellBall.translatedSpellProvider.setSpell(this.spell);

        assert source.getWorld() != null;
        source.getWorld().addEntity(spellBall);
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
        try
        {
            this.vec = new TowardsClause().parse(reader);
        }catch (CommandSyntaxException ignored)
        {
            this.vec = null;
        }

        reader.skipWhitespace();
        try
        {
            if (reader.readStringUntil(' ').equals(SpellModuleRegistries.SpellKeyWords.WITH_SPEED.get().toString()))
            {
                this.speed = reader.readDouble();
            }
        }
        catch (CommandSyntaxException e)
        {
            this.speed = 0;
        }
        reader.skipWhitespace();
        try
        {
            this.spell = TranslatedSpell.fromRawSpell(new RawSpell("", reader.readString(), null));
            this.provider.setSpell(this.spell);
        }
        catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            return false;
        }
        reader.skipWhitespace();
        try
        {
            this.maxLoop = reader.readInt();
        }catch (CommandSyntaxException e)
        {
            this.maxLoop = 20;
        }

        return true;
    }

    @Override
    public double guessManaCost(SpellCasterSource source)
    {
        return this.getManaCost(source);
    }
}
