package com.ustctuixue.arcaneart.api.util;

import net.minecraft.entity.Entity;

import java.util.stream.Collectors;

public class EntityList extends IteratingNonNullList<Entity>
{
    public Vec3dList getPositions()
    {
        Vec3dList nonNullList = new Vec3dList();
        nonNullList.addAll(this.stream().map(Entity::getPositionVec).collect(Collectors.toList()));
        return nonNullList;
    }
}
