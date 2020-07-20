package com.ustctuixue.arcaneart.api.spell.compiler.argument.vector3d;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class Vec3dArgument implements ArgumentType<RelativeVec3dBuilder>
{
    @Override
    public RelativeVec3dBuilder parse(StringReader reader) throws CommandSyntaxException
    {
        return null;
    }
}
