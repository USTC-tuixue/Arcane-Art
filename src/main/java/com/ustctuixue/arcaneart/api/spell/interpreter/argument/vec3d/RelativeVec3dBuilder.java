package com.ustctuixue.arcaneart.api.spell.interpreter.argument.vec3d;

import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import net.minecraft.util.math.Vec3d;

public class RelativeVec3dBuilder implements IRelativeArgumentBuilder<Vec3d>
{
    Vec3d pivot;
    Vec3d destination;
    @Override
    public Vec3d build(SpellBuilder source)
    {
        return null;
    }
}
