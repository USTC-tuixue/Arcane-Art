package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import lombok.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Map;

@AllArgsConstructor
public class SpellCasterSource
{
    @NonNull @Getter
    private final Vec3d pos;

    @NonNull @Getter
    private final Vec2f rotation;

    @NonNull @Getter
    private final World world;

    @NonNull @Getter
    private final MinecraftServer server;

    @Nullable @Getter
    private final Entity entity;

    @Nullable @Getter
    private final IManaBar manaBar;

    @Nullable @Getter
    private final ItemStack stack;

    @Nullable @Getter
    private final MPStorage mpStorage;


    public SpellCasterSource(
            Vec3d posIn, Vec2f rotationIn, World worldIn,
            MinecraftServer serverIn,
            @Nullable Entity entityIn, @Nullable MPStorage mpStorageIn)
    {
        this.pos = posIn;
        this.rotation = rotationIn;
        this.world = worldIn;
        this.server = serverIn;
        this.entity = entityIn;
        if (entityIn != null)
        {
            LazyOptional<IManaBar> optional = entityIn.getCapability(CapabilityMP.MANA_BAR_CAP);
            boolean hasManaBar = optional.isPresent();
            manaBar = hasManaBar? optional.orElseGet(DefaultManaBar::new) : null;
            stack = (entityIn instanceof LivingEntity)?((LivingEntity) entityIn).getActiveItemStack():null;
        }
        else
        {
            manaBar = null;
            stack = null;
        }
        this.mpStorage = mpStorageIn;
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
}
