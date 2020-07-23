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

    @Override
    public Vec3dList build(@Nonnull SpellCasterSource source)
    {
        Vec3d pivot = pivotBuilder.build(source).next();
        Vec3dList directions = targetBuilder.build(source);
        directions = directions.vectorAdd(pivot.inverse());
        return directions.transform(Vec3d::normalize);
    }
}
