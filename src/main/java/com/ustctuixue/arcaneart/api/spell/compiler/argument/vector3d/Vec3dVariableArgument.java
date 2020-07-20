package com.ustctuixue.arcaneart.api.spell.compiler.argument.vector3d;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.VariableArgument;

public class Vec3dVariableArgument extends VariableArgument<RelativeVec3dBuilder>
{
    @Override
    protected Class<RelativeVec3dBuilder> getType()
    {
        return RelativeVec3dBuilder.class;
    }

    @Override
    protected ArgumentType<RelativeVec3dBuilder> getArgumentType()
    {
        return new Vec3dArgument();
    }
}
