package com.ustctuixue.arcaneart.api.util;

import net.minecraft.util.math.Vec3d;

import java.util.stream.Collectors;

public class Vec3dList extends IteratingNonNullList<Vec3d>
{
    /**
     *
     * @param vec vec to add
     * @return new list containing results
     */
    public Vec3dList vectorAdd(Vec3d vec)
    {
        Vec3dList result = new Vec3dList();
        result.addAll(this.stream().map(vec3d -> vec3d.add(vec)).collect(Collectors.toList()));
        return result;
    }

}
