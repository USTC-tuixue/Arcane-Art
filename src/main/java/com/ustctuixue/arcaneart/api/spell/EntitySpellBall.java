package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.APIRegistries;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

public class EntitySpellBall extends Entity{
    //建议直接转extends Entity，告辞

    public LivingEntity shootingEntity;

    @Getter @Setter
    protected double gravityFactor;//0<=gravityFactor<=1.0. preserved. 为2.0的受重力影响法球预留
    
    protected void registerData() {
    }

    public void writeAdditional(CompoundNBT compound) {
        //TODO
        //把实体数据写进compound，可以看DamagingProjectileEntity
        Vec3d vec3d = this.getMotion();
        compound.put("direction", this.newDoubleNBTList(new double[]{vec3d.x, vec3d.y, vec3d.z}));
        //compound.put("power", this.newDoubleNBTList(new double[]{this.accelerationX, this.accelerationY, this.accelerationZ}));
        //compound.putInt("life", this.ticksAlive);
    }

    public void readAdditional(CompoundNBT compound) {
        /*
        if (compound.contains("power", 9)) {
            ListNBT listnbt = compound.getList("power", 6);
            if (listnbt.size() == 3) {
                this.accelerationX = listnbt.getDouble(0);
                this.accelerationY = listnbt.getDouble(1);
                this.accelerationZ = listnbt.getDouble(2);
            }
        }

        this.ticksAlive = compound.getInt("life");
        if (compound.contains("direction", 9) && compound.getList("direction", 6).size() == 3) {
            ListNBT listnbt1 = compound.getList("direction", 6);
            this.setMotion(listnbt1.getDouble(0), listnbt1.getDouble(1), listnbt1.getDouble(2));
        } else {
            this.remove();
        }
        */
        //从compound加载实体数据
    }

    public IPacket<?> createSpawnPacket() {
        //int i = this.shootingEntity == null ? 0 : this.shootingEntity.getEntityId();
        //return new SSpawnObjectPacket(this.getEntityId(), this.getUniqueID(), this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationPitch, this.rotationYaw, this.getType(), i, new Vec3d(this.accelerationX, this.accelerationY, this.accelerationZ));

        return new SSpawnObjectPacket(this);
        //我不知道这是干啥的
    }



    private ITranslatedSpellProvider translatedSpellProvider = new ITranslatedSpellProvider.Impl();

    @Getter @Setter
    protected IMPConsumer mpSource = null;
    @Getter @Setter
    protected int tier;

    @OnlyIn(Dist.DEDICATED_SERVER)
    private final SpellCasterSource source = createSource();

    @OnlyIn(Dist.DEDICATED_SERVER)
    private SpellCasterSource createSource(){
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

    //留给附属模组的接口，空构造函数
    public EntitySpellBall(EntityType<? extends EntitySpellBall> entitySpellBallEntityType, World world){
        super(entitySpellBallEntityType, world);
    }

    //推荐使用构造器来构造法球实体
    private EntitySpellBall(Builder builder){
        super(APIRegistries.Entities.SPELL_BALL_TYPE, builder.world);
        this.setLocationAndAngles(builder.x, builder.y, builder.z, builder.yaw, builder.pitch);

    }

    public static class Builder{
        private World world;
        private double x, y, z;
        private double gravityFactor;
        private LivingEntity shooter;
        private float yaw, pitch;

         public Builder (World world){
             this.world =world;
         }
         public Builder pos(double x, double y, double z){
             this.x = x;
             this.y = y;
             this.z = z;
             return this;
         }
         public Builder shooter(LivingEntity shooter){
             this.shooter = shooter;
             return this;
         }
         public Builder gravity(double gravityFactor){
             this.gravityFactor = gravityFactor;
             return this;
         }
         public Builder angles(float yaw, float pitch){
             this.yaw = yaw;
             this.pitch = pitch;
             return this;
         }
         public EntitySpellBall build(){
             return new EntitySpellBall(this);
         }
    }

    /*
    public EntitySpellBall(World worldIn, double x, double y, double z, double gravityFactor) {
        super(EntityType.FIREBALL, worldIn);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.gravityFactor = gravityFactor;
    }

    public EntitySpellBall(World worldIn, double x, double y, double z) {
        this(worldIn, x, y, z, 0.0D);
    }

    public EntitySpellBall(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityType.FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }
    */



    protected void onImpact(RayTraceResult result)
    {
        //super.onImpact(result);
        if (!this.world.isRemote){
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

    //static Field TICKS_ALIVE_FIELD;
    public int aliveTimer = 0;//生存时间的计时器。因为原生的是private所以还是自己实现一套比较好，或许还能算算距离
    public int maxTimer = 1000;//最大不衰减飞行时间，后面换成从cfg读

    @Override
    public void tick()
    {
        super.tick();
        if (!this.world.isRemote){
            //TODO
            //随时间而增加的能量消耗写在这
            aliveTimer ++;
            if(aliveTimer > maxTimer){
                //指数降低mp存量
            }
            this.translatedSpellProvider.getCompiled(source).executeOnHold(source);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side){
        if ((cap == CapabilitySpell.SPELL_CAP)||(cap == CapabilityMPStorage.MP_STORAGE_CAP)){
            return LazyOptional.of(() -> this.translatedSpellProvider).cast();
        }
        return super.getCapability(cap, side);
    }

}
