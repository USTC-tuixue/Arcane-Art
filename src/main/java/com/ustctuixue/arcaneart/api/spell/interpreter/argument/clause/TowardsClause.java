package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionVariableArgument;

public class TowardsClause extends Clause<Variable<DirectionBuilder>>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.TOWARDS;
    }

    @Override
    protected ArgumentType<Variable<DirectionBuilder>> getArgumentType()
    {
        return new DirectionVariableArgument();
    }

    @Override
    protected Variable<DirectionBuilder> defaultValue()
    {
        return new Variable<>(new DirectionBuilder());
    }
}
