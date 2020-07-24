package com.ustctuixue.arcaneart.api.util;

import net.minecraft.util.math.Vec3d;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
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
        return this.transform(vec3d -> vec3d.add(vec));
    }

    public Vec3dList transform(Function<? super Vec3d, ? extends Vec3d> transformer)
    {
        Vec3dList list = new Vec3dList();
        this.forEach( v ->
                list.add(transformer.apply(v))
        );
        return list;
    }

    public Vec3dList vectorScale(double factor)
    {
        return this.transform(vec3d -> vec3d.scale(factor));
    }
}
