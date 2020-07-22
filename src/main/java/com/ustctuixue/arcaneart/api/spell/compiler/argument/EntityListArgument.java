package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class EntityListArgument implements ArgumentType<NonNullList<Entity>>
{
    double distance;
    BlockPos originPos;


    @Override
    public NonNullList<Entity> parse(StringReader reader) throws CommandSyntaxException
    {

        return null;
    }
}
