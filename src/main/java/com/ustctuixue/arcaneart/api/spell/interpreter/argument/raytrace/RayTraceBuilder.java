package com.ustctuixue.arcaneart.api.spell.interpreter.argument.raytrace;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class RayTraceBuilder implements IRelativeArgumentBuilder<RayTraceResult>
{
    RayTraceContext context;
    @Override
    public RayTraceResult build(@Nonnull SpellCasterSource source)
    {
        World world = source.getWorld();
        return world.rayTraceBlocks(context);
    }
}
