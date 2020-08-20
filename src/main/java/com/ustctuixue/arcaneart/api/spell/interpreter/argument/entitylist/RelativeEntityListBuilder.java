package com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import com.ustctuixue.arcaneart.api.util.EntityList;
import com.ustctuixue.arcaneart.api.util.MinMaxBound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class RelativeEntityListBuilder implements IRelativeArgumentBuilder<EntityList>
{
    @Setter @Getter
    protected MinMaxBound<Double> distance = MinMaxBound.unBounded();
    @Setter
    protected RelativeVec3dListBuilder originPos = new RelativeVec3dListBuilder();
    @Setter @Getter
    protected int limit = 1;

    protected Predicate<Entity> predicate = entity -> true;
    @Setter
    protected boolean self = false;
    @Setter @Nullable
    protected AxisAlignedBB aabb = null;

    private EntityType<? extends Entity> type = null;

    public void setPredicate(Predicate<Entity> p)
    {
        this.predicate = p;
        if (p == IEntityPredicate.PLAYER)
        {
            this.type = EntityType.PLAYER;
        }
    }

    @Override
    public EntityList build(@Nonnull SpellCasterSource source)
    {
        EntityList list = new EntityList();

        if (self)
        {
            list.add(source.getEntity());
            ArcaneArtAPI.LOGGER.info("Targets: " + list);
            return list;
        }
        Vec3d pivot = originPos.build(source).next();

        if (aabb != null)
        {
            list.addAll(source.getWorld().getEntitiesWithinAABB(type, aabb.offset(pivot), this.summarizedPredicate(pivot)));
        }
        source.getWorld().getEntities(type, this.summarizedPredicate(pivot));
        ArcaneArtAPI.LOGGER.info("Targets: " + list);
        return list;
    }

    private Predicate<Entity> summarizedPredicate(Vec3d pos)
    {
        Predicate<Entity> filter = this.predicate;
        if (this.aabb != null)
        {
            AxisAlignedBB axisAlignedBB = aabb.offset(pos);
            filter = filter.and((entity -> axisAlignedBB.intersects(entity.getBoundingBox())));
        }

        if (!this.distance.isUnbounded())
        {
            filter = filter.and(entity -> this.distance.test(entity.getDistanceSq(pos)));
        }
        return filter;
    }
}
