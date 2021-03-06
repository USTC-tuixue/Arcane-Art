package com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.VariableArgument;

public class EntityListVariableArgument extends VariableArgument<RelativeEntityListBuilder>
{
    @Override
    protected Class<RelativeEntityListBuilder> getType()
    {
        return RelativeEntityListBuilder.class;
    }

    @Override
    protected ArgumentType<RelativeEntityListBuilder> getArgumentType()
    {
        return new EntityListArgument();
    }
}
