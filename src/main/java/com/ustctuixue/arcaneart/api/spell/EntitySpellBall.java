package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntitySpellBall extends DamagingProjectileEntity
{
    public static EntityType SPELL_BALL;
    protected EntitySpellBall(World p_i50173_2_)
    {
        super(SPELL_BALL, p_i50173_2_);
    }

    @OnlyIn(Dist.CLIENT)
    public EntitySpellBall(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(EntityType.FIREBALL, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public EntitySpellBall(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityType.FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        super.onImpact(result);
    }
}
