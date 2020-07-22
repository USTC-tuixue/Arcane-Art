package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.math.BlockPos;

public class BlockPosArgument implements ArgumentType<BlockPos>
{
    @Override
    public BlockPos parse(StringReader reader) throws CommandSyntaxException
    {
        return null;
    }
}
