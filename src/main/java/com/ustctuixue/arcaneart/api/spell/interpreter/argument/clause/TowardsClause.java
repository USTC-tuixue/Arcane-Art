package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.DirectionArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace.RayTraceBuilder;

public class TowardsClause extends Clause<RayTraceBuilder>
{
    @Override
    protected SpellKeyWord getInductor()
    {
        return SpellKeyWords.TOWARDS;
    }

    @Override
    protected ArgumentType<RayTraceBuilder> getArgumentType()
    {
        return new DirectionArgument();
    }

    @Override
    protected RayTraceBuilder defaultValue()
    {
        return new RayTraceBuilder();
    }
}
