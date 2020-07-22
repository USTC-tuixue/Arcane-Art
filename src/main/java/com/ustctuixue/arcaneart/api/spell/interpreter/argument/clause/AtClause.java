package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;

public class AtClause extends Clause<RelativeVec3dListBuilder>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.AT;
    }

    @Override
    protected ArgumentType<RelativeVec3dListBuilder> getArgumentType()
    {
        return new RelativeVec3dListArgument();
    }

    @Override
    protected RelativeVec3dListBuilder defaultValue()
    {
        return new RelativeVec3dListBuilder();
    }
}
