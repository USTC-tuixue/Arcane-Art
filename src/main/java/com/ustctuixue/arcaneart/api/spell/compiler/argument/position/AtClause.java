package com.ustctuixue.arcaneart.api.spell.compiler.argument.position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.Clause;
import net.minecraft.util.math.BlockPos;

public class AtClause extends Clause<RelativeBlockPosBuilder>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.AT;
    }

    @Override
    protected ArgumentType<RelativeBlockPosBuilder> getArgumentType()
    {
        return new RelativeBlockPosArgument();
    }

    @Override
    protected RelativeBlockPosBuilder defaultValue()
    {
        return RelativeBlockPosBuilder.DEFAULT_BUILDER;
    }
}
