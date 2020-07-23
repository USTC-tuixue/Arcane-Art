package com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause.FromClause;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;


public class DirectionArgument implements ArgumentType<DirectionBuilder>
{


    @Override
    public DirectionBuilder parse(StringReader reader) throws CommandSyntaxException
    {
        RelativeVec3dListBuilder targetList = new RelativeVec3dListArgument().parse(reader);
        RelativeVec3dListBuilder pivot = new FromClause().parse(reader).get();
        return new DirectionBuilder(targetList, pivot);
    }

}
