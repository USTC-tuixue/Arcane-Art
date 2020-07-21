package com.ustctuixue.arcaneart.api.spell.interpreter.argument.position;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Clause;

public class FromClause extends Clause<RelativeBlockPosBuilder>
{
    @Override
    protected RelativeBlockPosBuilder defaultValue()
    {
        return RelativeBlockPosBuilder.ZERO;
    }

    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.FROM;
    }

    @Override
    protected ArgumentType<RelativeBlockPosBuilder> getArgumentType()
    {
        return new RelativeBlockPosArgument();
    }
}
