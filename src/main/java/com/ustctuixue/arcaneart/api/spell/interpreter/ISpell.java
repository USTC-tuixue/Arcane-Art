package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.spell.modifier.ISpellCostModifier;

public interface ISpell
{
    /**
     * Get spell complexity according to spell source
     * @param source spell source
     * @return complexity
     */
    double getComplexityBase(SpellCasterSource source);

    default double getComplexity(SpellCasterSource source)
    {
        return getComplexityBase(source) * APIConfig.MP.COMPLEXITY_AMPLIFIER.get();
    }

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
        return ISpellCostModifier.modifyCost(getManaCostBase(source), source)
                * APIConfig.MP.MANA_COST_AMPLIFIER.get();
    }

    double guessManaCost(SpellCasterSource source);

}
