package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.math.Vec3d;

public class Vec3dArgument implements ArgumentType<Vec3d>
{
    @Override
    public Vec3d parse(StringReader reader) throws CommandSyntaxException
    {
        return null;
    }
}
