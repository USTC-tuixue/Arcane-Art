package com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import com.ustctuixue.arcaneart.api.util.Vec3dList;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

@Data @AllArgsConstructor
public class DirectionBuilder implements IRelativeArgumentBuilder<Vec3dList>
{
    private RelativeVec3dListBuilder targetBuilder;
    private RelativeVec3dListBuilder pivotBuilder;

    public DirectionBuilder()
    {
        targetBuilder = new RelativeVec3dListBuilder();
        pivotBuilder = new RelativeVec3dListBuilder();
    }

    @Nonnull
    @Override
    public Vec3dList build(@Nonnull SpellCasterSource source)
    {
        Vec3dList pivots = pivotBuilder.build(source);      // Absolute, origin = source
        Vec3dList targets = targetBuilder.build(source);    // Absolute, origin = source
        if (pivots.size() != 1)                             // When there more than 1 pivot, only use the first target
        {
            return pivots.transform(Vec3d::inverse).vectorAdd(targets.next())
                    .transform(Vec3d::normalize);
        }
        else                                                // When there is
        {
            Vec3d pivot = pivots.next();
            return targets.vectorAdd(pivot.inverse()).transform(Vec3d::normalize);
        }
    }

    public DirectionBuilder setPivots(RelativeVec3dListBuilder pivots)
    {
        this.pivotBuilder = pivots;
        return this;
    }
}
