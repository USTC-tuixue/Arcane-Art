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
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class EntitySpellBall extends Entity{
    //建议直接转extends Entity，告辞

    @Getter @Setter
    public LivingEntity shootingEntity;

    @Getter @Setter
    protected double gravityFactor;//0<=gravityFactor<=1.0. preserved. 为2.0的受重力影响法球预留

    public int ticksAlive = 0;//生存时间的计时器。
    public int maxTimer = 1000;//最大不衰减飞行时间，后面要换成从cfg读
    
    protected void registerData() {
    }

    public void writeAdditional(CompoundNBT compound) {
        //把实体的速度和寿命写进compound，可以看DamagingProjectileEntity
        Vec3d vec3d = this.getMotion();
        compound.put("direction", this.newDoubleNBTList(new double[]{vec3d.x, vec3d.y, vec3d.z}));
        //compound.put("power", this.newDoubleNBTList(new double[]{this.accelerationX, this.accelerationY, this.accelerationZ}));
        compound.putInt("life", this.ticksAlive);
    }

    public void readAdditional(CompoundNBT compound) {
        //从compound加载实体数据
        this.ticksAlive = compound.getInt("life");
        if (compound.contains("direction", 9) && compound.getList("direction", 6).size() == 3) {
            ListNBT listnbt1 = compound.getList("direction", 6);
            this.setMotion(listnbt1.getDouble(0), listnbt1.getDouble(1), listnbt1.getDouble(2));
        } else {
            this.remove();
        }
    }

    public IPacket<?> createSpawnPacket() {
        //int i = this.shootingEntity == null ? 0 : this.shootingEntity.getEntityId();
        //return new SSpawnObjectPacket(this.getEntityId(), this.getUniqueID(), this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationPitch, this.rotationYaw, this.getType(), i, new Vec3d(this.accelerationX, this.accelerationY, this.accelerationZ));

        return new SSpawnObjectPacket(this);
        //我不知道这是干啥的
    }

    /**
     * Checks if the entity is in range to render.
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
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
        this.setGravityFactor(builder.gravityFactor);
        this.setShootingEntity(builder.shooter);
        this.setMotion(new Vec3d(builder.vx,builder.vy,builder.vz));
    }

    public static class Builder{
        private World world;
        private double x, y, z;
        private double vx, vy, vz;
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
         public Builder motion(double vx, double vy, double vz){
             this.vx = vx;
             this.vy = vy;
             this.vz = vz;
             return this;
         }

         /*
         gives out a spell ball emit from a block, at the given direction & speed
          */
        public Builder emitFromBlock(BlockPos pos, Direction FACING, double speed){
            this.x = pos.getX() + 0.5D;
            this.y = pos.getY() + 0.5D;
            this.z = pos.getZ() + 0.5D;
            if(FACING == Direction.UP){
                this.y += 0.5;
                this.vy = speed;
            }
            else if(FACING == Direction.DOWN){
                this.y -= 0.5;
                this.vy = -speed;
            }
            else if(FACING == Direction.EAST){
                this.x += 0.5;
                this.vx = speed;
            }
            else if(FACING == Direction.WEST){
                this.x -= 0.5;
                this.vx = -speed;
            }
            else if(FACING == Direction.NORTH){
                this.z += 0.5;
                this.vz = speed;
            }
            else if(FACING == Direction.SOUTH){
                this.z -= 0.5;
                this.vz = -speed;
            }
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



    protected void onImpact(RayTraceResult result)
    {
        //super.onImpact(result);

        if (!this.world.isRemote){
            if(!this.translatedSpellProvider.hasSpell()){
                //插入作为能量传输手段的逻辑
                //和棱镜碰撞时转向（专门处理和xyz轴对齐的情况以提高效率）
                //棱镜分两种：直角棱镜和分光棱镜
                //不考虑任何折射，摸了
                //和MPStorage的te碰撞时赋予其能量
                if(result.getType() == RayTraceResult.Type.BLOCK){
                    //碰上方块了
                }
                else if(result.getType() == RayTraceResult.Type.ENTITY) {
                    //给实体补充能量
                }
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

        if (!this.world.isRemote) {

            ++this.ticksAlive;
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, true, this.ticksAlive >= 25, this.shootingEntity, RayTraceContext.BlockMode.COLLIDER);
            //这个includeShooter大概率是指在什么情况下允许射出投掷物的实体被投掷物击中，但是逻辑比较复杂，我读不太懂。此处照抄了DamagingProjectileEntity的逻辑

            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

            Vec3d vec3d = this.getMotion();
            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + vec3d.y;
            double d2 = this.getPosZ() + vec3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            //float f = this.getMotionFactor();
            /*
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
                }
                f = 0.8F;
            }
            */

            //this.setMotion(vec3d.add(this.accelerationX, this.accelerationY, this.accelerationZ).scale((double)f));
            //this.world.addParticle(this.getParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPosition(d0, d1, d2);

            //TODO
            //随时间而增加的能量消耗写在这
            ticksAlive++;
            if(ticksAlive > maxTimer){
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
