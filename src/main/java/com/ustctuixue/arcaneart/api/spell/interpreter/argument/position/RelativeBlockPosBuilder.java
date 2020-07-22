package com.ustctuixue.arcaneart.api.spell.interpreter.argument.position;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

@AllArgsConstructor @Data
public class RelativeBlockPosBuilder implements IRelativeArgumentBuilder<BlockPos>
{
    @Getter @Setter
    private BlockPos relativeCoordinate;

    public RelativeBlockPosBuilder()
    {
        relativeCoordinate = BlockPos.ZERO;
    }

    public RelativeBlockPosBuilder(int x, int y, int z)
    {
        relativeCoordinate = new BlockPos(x, y, z);
    }


    @Override
    public BlockPos build(@Nonnull SpellCasterSource source)
    {
        return new BlockPos(source.getPos().add(new Vec3d(relativeCoordinate)));
    }
}
