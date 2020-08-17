package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;

public class SummonSpellBallSpell implements ISpell
{
    /**
     * Get spell complexity according to spell source
     *
     * @param source spell source
     * @return complexity
     */
    @Override
    public double getComplexityBase(SpellCasterSource source)
    {
        return 0;
    }

    /**
     * @param source spell source
     * @return mana base
     */
    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        return 0;
    }

    /**
     * Execute spell
     *
     * @param source spell source
     */
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
        return false;
    }

    @Override
    public double guessManaCost(SpellCasterSource source)
    {
        return 0;
    }
}
