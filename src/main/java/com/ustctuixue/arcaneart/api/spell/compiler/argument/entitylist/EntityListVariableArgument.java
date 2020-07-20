package com.ustctuixue.arcaneart.api.spell.compiler.argument.entitylist;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.VariableArgument;

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
