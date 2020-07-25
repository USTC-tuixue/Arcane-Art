package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.spell.modifier.ISpellCostModifier;

public interface ISpell
{
    double getComplexity(SpellCasterSource source);
    double getManaCostBase(SpellCasterSource source);
    void execute(SpellCasterSource source);

    /**
     * parse spell into self
     *
     * @param reader reader
     * @return true if successfully parsed
     */
    boolean parse(StringReader reader);

    default double getManaCost(SpellCasterSource source)
    {
        return ISpellCostModifier.modifyCost(getManaCostBase(source), source);
    }

}
