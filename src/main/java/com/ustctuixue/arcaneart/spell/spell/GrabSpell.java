package com.ustctuixue.arcaneart.spell.spell;

import com.mojang.brigadier.StringReader;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionBuilder;

public class GrabSpell implements ISpell
{
    RelativeEntityListBuilder target;
    DirectionBuilder motion;
    double distance;

    /**
     * Get spell complexity according to spell source
     *
     * @param source spell source
     * @return complexity
     */
    @Override
    public double getComplexity(SpellCasterSource source)
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
}
