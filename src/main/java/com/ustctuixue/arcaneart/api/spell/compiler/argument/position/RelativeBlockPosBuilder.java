package com.ustctuixue.arcaneart.api.spell.compiler.argument.position;

import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.IRelativeArgumentBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.BlockPos;

@AllArgsConstructor
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
    public BlockPos build(SpellBuilder source)
    {
        return null;
    }

    public static RelativeBlockPosBuilder DEFAULT_BUILDER =
            new RelativeBlockPosBuilder(BlockPos.ZERO);
}
