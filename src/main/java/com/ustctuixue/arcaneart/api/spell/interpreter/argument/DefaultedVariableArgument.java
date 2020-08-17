package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.mojang.brigadier.arguments.ArgumentType;

public class DefaultedVariableArgument<V>
        extends DefaultedArgument<Variable<V>>
{

    public DefaultedVariableArgument(ArgumentType<Variable<V>> argumentParser, V defaultedValue)
    {
        super(argumentParser, new Variable<V>(defaultedValue));
    }
}
