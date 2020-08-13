package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.api.spell.SpellCasterTiers;
import lombok.*;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@AllArgsConstructor
public class SpellCasterSource
{
    @Getter
    private final Vec3d pos;

    @Getter
    private final Vec2f rotation;

    @Getter
    private final ServerWorld world;

    @Getter
    private final MinecraftServer server;

    /**
     * 直接执行法术的实体
     */
    @Getter
    private final Entity entity;

    /**
     * MP 的来源
     */
    @Getter
    private final IMPConsumer mpConsumer;

    @Getter
    private final double complexityTolerance;


    public SpellCasterSource(ServerWorld worldIn, @Nonnull Entity entityIn, @Nullable MPStorage mpStorageIn, double toleranceIn)
    {
        this(entityIn.getPositionVec(), entityIn.getPitchYaw(),
                worldIn,
                worldIn.getServer(), entityIn, mpStorageIn, toleranceIn
        );
    }

    public SpellCasterSource(
            Vec3d posIn, Vec2f rotationIn, ServerWorld worldIn,
            MinecraftServer serverIn,
            @Nullable Entity entityIn, @Nullable MPStorage mpStorageIn, double toleranceIn)
    {
        this.pos = posIn;
        this.rotation = rotationIn;
        this.world = worldIn;
        this.server = serverIn;
        this.entity = entityIn;
        this.complexityTolerance = toleranceIn;
        if (entityIn != null)
        {
            LazyOptional<IManaBar> optionalManaBar = entityIn.getCapability(CapabilityMP.MANA_BAR_CAP);
            boolean hasManaBar = optionalManaBar.isPresent();
            this.mpConsumer = hasManaBar? optionalManaBar.orElseGet(DefaultManaBar::new) : null;
        }
        else
        {
            this.mpConsumer = mpStorageIn;
        }
    }

    @Getter
    private final Map<String, Object> variables = Maps.newHashMap();

    /**
     *
     * @param name name of variable
     * @param <T> variable type
     * @return variable value if exists and type-consistent, or else null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getVariable(String name, Class<? extends T> clazz)
    {
        Object v = this.variables.get(name);
        return clazz.isInstance(v) ? (T) v : null;
    }

    public void setVariable(String name, Object obj)
    {
        variables.put(name, obj);
    }

    public static SpellCasterSource fromCommandSource(CommandSource source)
    {
        return new SpellCasterSource(
                source.getPos(), source.getRotation(), source.getWorld(), source.getServer(),
                source.getEntity(), null, SpellCasterTiers.MAX_TIER
        );
    }

}
