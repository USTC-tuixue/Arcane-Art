package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListVariableArgument;

public class FromClause extends Clause<Variable<RelativeVec3dListBuilder>>
{
    @Override
    protected Variable<RelativeVec3dListBuilder> defaultValue()
    {
        return new Variable<>(new RelativeVec3dListBuilder());
    }

    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.FROM;
    }

    @Override
    protected ArgumentType<Variable<RelativeVec3dListBuilder>> getArgumentType()
    {
        return new RelativeVec3dListVariableArgument();
    }
}
