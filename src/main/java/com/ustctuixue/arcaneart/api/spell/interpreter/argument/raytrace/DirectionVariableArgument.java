package com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.VariableArgument;

public class DirectionVariableArgument extends VariableArgument<DirectionBuilder>
{
    @Override
    protected Class<DirectionBuilder> getType()
    {
        return DirectionBuilder.class;
    }

    @Override
    protected ArgumentType<DirectionBuilder> getArgumentType()
    {
        return new DirectionArgument();
    }
}
