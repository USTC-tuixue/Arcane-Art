package com.ustctuixue.arcaneart.api.spell.compiler.argument.entitylist;

import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.util.MinMaxBound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class RelativeEntityListBuilder implements IRelativeArgumentBuilder<EntityList>
{
    @Setter @Getter
    protected MinMaxBound<Double> distance = MinMaxBound.unBounded();
    @Setter
    protected BlockPos originPos = BlockPos.ZERO;
    @Setter
    protected int limit = 1;
    @Setter
    protected Predicate<Entity> predicate = entity -> true;
    @Setter
    protected boolean self = false;
    @Setter @Nullable
    protected AxisAlignedBB aabb = null;


    @Override
    public EntityList build(SpellBuilder source)
    {
        return null;
    }
}
