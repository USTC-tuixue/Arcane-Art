package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.APIRegistries;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class EntitySpellBall extends DamagingProjectileEntity
{
    private ITranslatedSpellProvider translatedSpellProvider = new ITranslatedSpellProvider.Impl();

    @Getter @Setter
    protected IMPConsumer mpSource = null;
    @Getter @Setter
    protected int tier;

    @OnlyIn(Dist.DEDICATED_SERVER)
    private final SpellCasterSource source = createSource();

    @OnlyIn(Dist.DEDICATED_SERVER)
    private SpellCasterSource createSource()
    {
        return new SpellCasterSource(
                this.getPositionVec(), this.getPitchYaw(),
                (ServerWorld) this.world,
                this.world.getServer(), this, this.mpSource, tier
        );
    }

    protected EntitySpellBall(World p_i50173_2_)
    {
        super(APIRegistries.Entities.SPELL_BALL_TYPE, p_i50173_2_);
    }

    @OnlyIn(Dist.CLIENT)
    public EntitySpellBall(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(EntityType.FIREBALL, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public EntitySpellBall(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityType.FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }

    public EntitySpellBall(EntityType<EntitySpellBall> entitySpellBallEntityType, World world)
    {
        super(entitySpellBallEntityType, world);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        super.onImpact(result);
        if (!this.world.isRemote)
        {
            if(!this.translatedSpellProvider.hasSpell()){
                //插入作为能量传输手段的逻辑
                //和棱镜碰撞时转向（专门处理和xyz轴对齐的情况以提高效率，是否要考虑非全反射？）
                //和MPStorage的te碰撞时赋予其能量
                return;
            };
            TranslatedSpell spell = this.translatedSpellProvider.getSpell();
            this.translatedSpellProvider.getCompiled(source).executeOnRelease(source);
        }
    }



    @Override
    public void tick()
    {
        super.tick();
        if (!this.world.isRemote)
        {
            //随时间而增加的能量消耗写在这（划掉）
            //this.ticksAlive;（划掉）
            //不要写在tick里面，在创建实体事件里面安排计划刻
            this.translatedSpellProvider.getCompiled(source).executeOnHold(source);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        if (cap == CapabilitySpell.SPELL_CAP)
        {
            return LazyOptional.of(() -> this.translatedSpellProvider).cast();
        }
        return super.getCapability(cap, side);
    }

}
