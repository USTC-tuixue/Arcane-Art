package com.ustctuixue.arcaneart.api.spell.compiler.argument.position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.compiler.CommandExceptionTypes;
import net.minecraft.util.math.BlockPos;

public class RelativeBlockPosArgument implements ArgumentType<RelativeBlockPosBuilder>
{
    @Override
    public RelativeBlockPosBuilder parse(StringReader reader) throws CommandSyntaxException
    {
        int x, y, z;
        reader.skipWhitespace();
        x = reader.readInt();
        reader.skipWhitespace();
        y = reader.readInt();
        reader.skipWhitespace();
        z = reader.readInt();
        return new RelativeBlockPosBuilder(x, y, z);

    }
}
