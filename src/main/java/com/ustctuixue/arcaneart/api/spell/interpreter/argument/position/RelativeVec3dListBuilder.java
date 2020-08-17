package com.ustctuixue.arcaneart.api.spell.interpreter.argument.position;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.util.Vec3dList;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data @AllArgsConstructor
public class RelativeVec3dListBuilder implements IRelativeArgumentBuilder<Vec3dList>
{
    private final Vec3dList relativeVec = new Vec3dList();
    @Nullable
    private RelativeEntityListBuilder entityListBuilder = null;

    public RelativeVec3dListBuilder()
    {
        relativeVec.add(Vec3d.ZERO);
    }

    public RelativeVec3dListBuilder(double x, double y, double z)
    {
        relativeVec.add(new Vec3d(x, y, z));
    }

    @Override
    public Vec3dList build(@Nonnull SpellCasterSource source)
    {
        if (entityListBuilder != null)
        {
            return entityListBuilder.build(source).getPositions();
        }
        return this.relativeVec.vectorAdd(source.getPos());
    }

    public boolean isFromEntity()
    {
        return entityListBuilder != null;
    }
}
