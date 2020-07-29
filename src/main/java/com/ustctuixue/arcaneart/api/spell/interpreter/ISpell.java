package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.spell.modifier.ISpellCostModifier;

public interface ISpell
{
    /**
     * Get spell complexity according to spell source
     * @param source spell source
     * @return complexity
     */
    double getComplexity(SpellCasterSource source);

    /**
     *
     * @param source spell source
     * @return mana base
     */
    double getManaCostBase(SpellCasterSource source);

    /**
     * Execute spell
     *
     * @param source spell source
     */
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
