package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionBuilder;

public class TowardsClause extends Clause<DirectionBuilder>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.TOWARDS;
    }

    @Override
    protected ArgumentType<DirectionBuilder> getArgumentType()
    {
        return new DirectionArgument();
    }

    @Override
    protected DirectionBuilder defaultValue()
    {
        return new DirectionBuilder();
    }
}
