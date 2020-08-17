package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListVariableArgument;

public class AtClause extends Clause<Variable<RelativeVec3dListBuilder>>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.AT;
    }

    @Override
    protected RelativeVec3dListVariableArgument getArgumentType()
    {
        return new RelativeVec3dListVariableArgument();
    }

    @Override
    protected Variable<RelativeVec3dListBuilder> defaultValue()
    {
        return new Variable<>(new RelativeVec3dListBuilder());
    }
}
