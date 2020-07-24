package com.ustctuixue.arcaneart.api.spell.interpreter.argument.position;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.VariableArgument;

public class RelativeVec3dListVariableArgument extends VariableArgument<RelativeVec3dListBuilder>
{
    @Override
    protected Class<RelativeVec3dListBuilder> getType()
    {
        return RelativeVec3dListBuilder.class;
    }

    @Override
    protected ArgumentType<RelativeVec3dListBuilder> getArgumentType()
    {
        return new RelativeVec3dListArgument();
    }
}
