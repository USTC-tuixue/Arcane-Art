package com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause.FromClause;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dBuilder;
import com.ustctuixue.arcaneart.api.util.Vec3dList;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;

public class DirectionArgument implements ArgumentType<RayTraceBuilder>
{
    private RelativeVec3dBuilder targetList;
    private RelativeVec3dBuilder pivotList;

    @Override
    public RayTraceBuilder parse(StringReader reader) throws CommandSyntaxException
    {
        targetList = new RelativeVec3dArgument().parse(reader);
        pivotList = new FromClause().parse(reader);
        return new RayTraceBuilder();
    }

}
