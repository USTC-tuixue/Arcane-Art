package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.spell.SpellCasterTiers;
import lombok.*;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

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

    @Getter
    private final Entity entity;

    @Getter
    private final IMPConsumer mpConsumer;

    @Getter
    private final ItemStack spellCasterStack;

    @Getter
    private final int casterTier;


    public SpellCasterSource(World worldIn, @Nonnull Entity entityIn, @Nullable MPStorage mpStorageIn, int tierIn)
    {
        this(entityIn.getPositionVec(), entityIn.getPitchYaw(),
                Objects.requireNonNull(worldIn.getServer()).getWorld(worldIn.getDimension().getType()),
                worldIn.getServer(), entityIn, mpStorageIn, tierIn
        );
    }

    public SpellCasterSource(
            Vec3d posIn, Vec2f rotationIn, ServerWorld worldIn,
            MinecraftServer serverIn,
            @Nullable Entity entityIn, @Nullable MPStorage mpStorageIn, int tierIn)
    {
        this.pos = posIn;
        this.rotation = rotationIn;
        this.world = worldIn;
        this.server = serverIn;
        this.entity = entityIn;
        this.casterTier = tierIn;
        if (entityIn != null)
        {
            LazyOptional<IManaBar> optionalManaBar = entityIn.getCapability(CapabilityMP.MANA_BAR_CAP);
            boolean hasManaBar = optionalManaBar.isPresent();
            this.mpConsumer = hasManaBar? optionalManaBar.orElseGet(DefaultManaBar::new) : null;
            spellCasterStack = (entityIn instanceof LivingEntity)?((LivingEntity) entityIn).getActiveItemStack():null;
        }
        else
        {
            this.mpConsumer = mpStorageIn;
            spellCasterStack = null;
        }
    }

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
                source.getEntity(), null, null, SpellCasterTiers.MAX_TIER
        );
    }
}
