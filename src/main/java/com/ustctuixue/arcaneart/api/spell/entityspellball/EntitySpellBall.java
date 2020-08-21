package com.ustctuixue.arcaneart.api.spell.entityspellball;

import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.APIRegistries;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.api.spell.CapabilitySpell;
import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxReflector;
import com.ustctuixue.arcaneart.automation.luxtransport.LuxSplitter;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class EntitySpellBall extends Entity{
    //直接转extends Entity，告辞

    public static double HALF_SIZE = 0.25;//法球y轴高度的一半

    @Getter @Setter
    public LivingEntity shootingEntity;

    /*
    @Getter @Setter
    protected float gravityFactor = 0;//0<=gravityFactor<=1.0. preserved. 为2.0的受重力影响法球预留
    public int ticksAlive = 0;
     */

    private static final DataParameter<Integer> TICKS_ALIVE = EntityDataManager.createKey(EntitySpellBall.class, DataSerializers.VARINT);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntitySpellBall.class, DataSerializers.FLOAT);

    public int maxTimer = APIConfig.Spell.MAX_LIFE_TIME.get();//最大不衰减飞行时间
    public double descendingRate = APIConfig.Spell.DESCENDING_RATE.get();//每tick衰减量

    public ITranslatedSpellProvider translatedSpellProvider = new ITranslatedSpellProvider.Impl();
    public MPStorage spellBallMPStorage;




    @Override
    protected void registerData() {
        this.dataManager.register(TICKS_ALIVE, 0);
        this.dataManager.register(GRAVITY, 0F);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        //把实体的速度和寿命写进compound，可以看DamagingProjectileEntity
        Vec3d vec3d = this.getMotion();
        compound.put("direction", this.newDoubleNBTList(vec3d.x, vec3d.y, vec3d.z));
        //compound.put("power", this.newDoubleNBTList(new double[]{this.accelerationX, this.accelerationY, this.accelerationZ}));
        compound.putInt("life", this.dataManager.get(TICKS_ALIVE));
        LazyOptional<ITranslatedSpellProvider> spellCap = this.getCapability(CapabilitySpell.SPELL_CAP);
        spellCap.ifPresent((s) -> {
            compound.put("spell", Objects.requireNonNull(new CapabilitySpell.Storage().writeNBT(CapabilitySpell.SPELL_CAP, this.translatedSpellProvider, null)));
        });
        LazyOptional<MPStorage> mpCap = this.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
        mpCap.ifPresent((s) -> {
            compound.put("mpstorage", Objects.requireNonNull(new CapabilityMPStorage.Storage().writeNBT(CapabilityMPStorage.MP_STORAGE_CAP, this.spellBallMPStorage, null)));
        });

    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        //从compound加载实体数据
        //this.ticksAlive = compound.getInt("life");
        this.dataManager.set(TICKS_ALIVE, compound.getInt("life"));
        if (compound.contains("direction", 9) && compound.getList("direction", 6).size() == 3) {
            ListNBT listnbt1 = compound.getList("direction", 6);
            this.setMotion(listnbt1.getDouble(0), listnbt1.getDouble(1), listnbt1.getDouble(2));
        } else {
            this.remove();
        }
        LazyOptional<ITranslatedSpellProvider> spellCap = this.getCapability(CapabilitySpell.SPELL_CAP);
        spellCap.ifPresent((s) -> {
            new CapabilitySpell.Storage().readNBT(CapabilitySpell.SPELL_CAP, s, null, compound.get("spell"));
        });
        LazyOptional<MPStorage> mpCap = this.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
        mpCap.ifPresent((s) -> {
            new CapabilityMPStorage.Storage().readNBT(CapabilityMPStorage.MP_STORAGE_CAP, s, null, compound.get("mpstorage"));
        });
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        //int i = this.shootingEntity == null ? 0 : this.shootingEntity.getEntityId();
        //return new SSpawnObjectPacket(this.getEntityId(), this.getUniqueID(), this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationPitch, this.rotationYaw, this.getType(), i, new Vec3d(this.accelerationX, this.accelerationY, this.accelerationZ));

        return NetworkHooks.getEntitySpawningPacket(this);
        // 生成实体要发送数据包
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

    @Getter @Setter
    protected IMPConsumer mpSource = null;
    @Getter @Setter
    protected int tier;

    //@OnlyIn(Dist.DEDICATED_SERVER)
    private final SpellCasterSource source = createSource();

    //@OnlyIn(Dist.DEDICATED_SERVER)
    private SpellCasterSource createSource(){
        if (!this.world.isRemote) {
            return new SpellCasterSource(
                    this.getPositionVec(), this.getPitchYaw(),
                    (ServerWorld) this.world,
                    this.world.getServer(), this, this.mpSource, tier
            );
        }
        else {
            return new SpellCasterSource(
                    this.getPositionVec(), this.getPitchYaw(),
                    null,
                    this.world.getServer(), this, this.mpSource, tier
            );
        }
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
        this.dataManager.set(GRAVITY, builder.gravityFactor);
        this.setShootingEntity(builder.shooter);
        this.setMotion(new Vec3d(builder.vx,builder.vy,builder.vz));
        this.spellBallMPStorage = builder.mps;
    }

    public static class Builder{
        private World world;
        private double x, y, z;
        private double vx, vy, vz;
        private float gravityFactor;
        private LivingEntity shooter;
        private float yaw, pitch;
        private MPStorage mps;

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
         public Builder motion(Vec3d motion){
             return motion(motion.getX(), motion.getY(), motion.getZ());
         }

         /*
         gives out a spell ball emit from a block, at the given direction & speed
          */
         public Builder emitFromBlock(BlockPos pos, Direction FACING, double speed){
             this.x = pos.getX() + 0.5D;
             this.y = pos.getY() + 0.5D - HALF_SIZE;
             this.z = pos.getZ() + 0.5D;
             double len = 1;//向外平移多少以免撞上发射器
             if(FACING == Direction.UP){
                 this.y += len;
                 this.vy = speed;
                 this.pitch = +90;
             }
             else if(FACING == Direction.DOWN){
                 this.y -= len;
                 this.vy = -speed;
                 this.pitch = -90;
             }
             else if(FACING == Direction.EAST){
                 this.x += len;
                 this.vx = speed;
                 this.yaw = 90;
             }
             else if(FACING == Direction.WEST){
                 this.x -= len;
                 this.vx = -speed;
                 this.yaw = 270;
             }
             else if(FACING == Direction.SOUTH){
                 this.z += len;
                 this.vz = speed;
                 this.yaw = 180;
             }
             else if(FACING == Direction.NORTH){
                 this.z -= len;
                 this.vz = -speed;
                 this.yaw = 0;
             }
             /*
             for(PlayerEntity p : world.getPlayers()){
                 p.sendMessage(new StringTextComponent(vx + "," + vy + "," + vz + "," + x + "," + y + "," + z));
             }//测试
              */
             return this;
         }
         public Builder shooter(LivingEntity shooter){
             this.shooter = shooter;
             return this;
         }
         public Builder gravity(float gravityFactor){
             this.gravityFactor = gravityFactor;
             return this;
         }
         public Builder angles(float yaw, float pitch){
             this.yaw = yaw;
             this.pitch = pitch;
             return this;
         }

         /*
         gives out a spell ball with full mp
          */
         public Builder setFullMP(double maxMP) {
             MPStorage mps = new MPStorage();
             mps.setMaxMana(maxMP);
             mps.setMana(maxMP);
             this.mps = mps;
             return this;
         }

        public Builder setMP(double mana, double maxMP) {
            MPStorage mps = new MPStorage();
            mps.setMaxMana(maxMP);
            mps.setMana(mana);
            this.mps = mps;
            return this;
        }
         public EntitySpellBall build(){
             return new EntitySpellBall(this);
         }
    }



    protected void onImpact(RayTraceResult result){
        //super.onImpact(result);

        //if (!this.world.isRemote) {

            //没带法术，插入作为能量传输手段的逻辑
            //和棱镜碰撞时转向（专门处理和xyz轴对齐的情况以提高效率）
            //棱镜分两种：直角棱镜和分光棱镜，直角棱镜和台阶类似，分光棱镜是两个直角棱镜捏在一起的正方体
            //不考虑任何折射，摸了
            //和MPStorage的te碰撞时赋予其能量


            if (result.getType() == RayTraceResult.Type.BLOCK) {
                //碰上方块了
                //BlockPos pos = ((BlockRayTraceResult) result).getPos();
                //BlockState block = world.getBlockState(pos);

                BlockPos pos = ((BlockRayTraceResult) result).getPos().offset(((BlockRayTraceResult) result).getFace());
                BlockState block = world.getBlockState(pos);

                /*
                for(PlayerEntity p : world.getPlayers()){
                    p.sendMessage(new StringTextComponent(pos.getX() + "," + pos.getY() + "," + pos.getZ()));
                }//测试
                 */



                if (block.getBlock() instanceof LuxReflector) {
                    this.reflect(block.get(LuxReflector.FACING), pos);
                    //TODO
                    // 设置红石信号强度的blockstate
                    block.updateNeighbors(world, pos, 3);
                    //必须更新才能正常反应红石信号
                    //这个flag堪称mojang硬编码艺术的核心，最高128，用不同位的0/1来控制更新类型
                    //https://www.bilibili.com/read/cv4565671/
                    //net\minecraft\world\IWorldWriter.java line 9-19
                }
                else if (block.getBlock() instanceof LuxSplitter) {
                    this.split(block.get(LuxSplitter.FACING), pos);
                    //TODO
                    // 设置红石信号强度的blockstate
                    block.updateNeighbors(world, pos, 3);
                    //必须更新才能正常反应红石信号
                    //这个flag堪称mojang硬编码艺术的核心，最高128，用不同位的0/1来控制更新类型
                    //https://www.bilibili.com/read/cv4565671/
                    //net\minecraft\world\IWorldWriter.java line 9-19
                }
                else if (true){
                    //getPos拿到的是在撞上之前最后一个经过的方块（或实体所在的blockpos），因此后面施法和充能需要判断是不是已经在方块内
                    //把那个if true替换成“若该实体和方块有碰撞箱重叠”，然后把下面两句调出来
                    //BlockPos pos = ((BlockRayTraceResult) result).getPos();
                    //BlockState block = world.getBlockState(pos);
                    if (this.translatedSpellProvider.hasSpell()) {
                        //执行瞬时施法操作
                        TranslatedSpell spell = this.translatedSpellProvider.getSpell();
                        this.translatedSpellProvider.getCompiled(source).executeOnRelease(source);
                        LivingEntity shooter = this.getShootingEntity();
                        LazyOptional<IManaBar> optionalManaBar = shooter.getCapability(CapabilityMP.MANA_BAR_CAP);
                        optionalManaBar.ifPresent((s) -> {
                            double mana = s.getMana();
                            double maxMP = s.getMaxMana((LivingEntity) shooter);
                            double leftOverMana = this.spellBallMPStorage.getMana();
                            if (mana + leftOverMana > maxMP) {
                                s.setMana(maxMP);
                            } else {
                                s.setMana(mana + leftOverMana);
                            }
                        });//return the leftover mana to the caster
                    }
                    else if (block.hasTileEntity()) {
                        TileEntity te = world.getTileEntity(pos);
                        assert te != null;
                        LazyOptional<MPStorage> mpStorageCapLazyOptional = te.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
                        mpStorageCapLazyOptional.ifPresent((s) -> {
                            double mana = s.getMana();
                            double maxMP = s.getMaxMana();
                            double spellMana = this.spellBallMPStorage.getMana();
                            if (mana + spellMana > maxMP) {
                                s.setMana(maxMP);
                            }
                            else {
                                s.setMana(mana + spellMana);
                            }
                        });
                    }
                    this.spellBallMPStorage.setMana(0D);//delete this spell ball
                }


            }
            else if (result.getType() == RayTraceResult.Type.ENTITY) {
                if (this.translatedSpellProvider.hasSpell()) {
                    //执行瞬时施法操作
                    TranslatedSpell spell = this.translatedSpellProvider.getSpell();
                    this.translatedSpellProvider.getCompiled(source).executeOnRelease(source);
                    LivingEntity shooter = this.getShootingEntity();
                    LazyOptional<IManaBar> optionalManaBar = shooter.getCapability(CapabilityMP.MANA_BAR_CAP);
                    optionalManaBar.ifPresent((s) -> {
                        double mana = s.getMana();
                        double maxMP = s.getMaxMana((LivingEntity) shooter);
                        double leftOverMana = this.spellBallMPStorage.getMana();
                        if (mana + leftOverMana > maxMP) {
                            s.setMana(maxMP);
                        }
                        else {
                            s.setMana(mana + leftOverMana);
                        }
                    });//return the leftover mana to the caster
                    this.spellBallMPStorage.setMana(0D);//delete this spell ball
                }
                else {
                    //没有携带法术，给实体补充能量
                    Entity entity = ((EntityRayTraceResult) result).getEntity();
                    if (entity instanceof LivingEntity) {
                        LazyOptional<IManaBar> optionalManaBar = entity.getCapability(CapabilityMP.MANA_BAR_CAP);
                        optionalManaBar.ifPresent((s) -> {
                            double mana = s.getMana();
                            double maxMP = s.getMaxMana((LivingEntity) entity);
                            double spellMana = this.spellBallMPStorage.getMana();
                            if (mana + spellMana > maxMP) {
                                s.setMana(maxMP);
                            } else {
                                s.setMana(mana + spellMana);
                            }
                        });
                    }
                    this.spellBallMPStorage.setMana(0D);//delete this spell ball
                }

            }
        //}
    }


    @Override
    public void tick(){
        super.tick();

        if (this.world.isRemote){
            /*
            for(PlayerEntity p : world.getPlayers()){
                p.sendMessage(new StringTextComponent("ticking spell client" + this.getPosX() + "," + this.getPosY() + "," + this.getPosZ()));
            }//测试
            */
            Vec3d vec3d = this.getMotion();
            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + HALF_SIZE + vec3d.y;//中心坐标
            double d2 = this.getPosZ() + vec3d.z;

            for(int i = 0; i < 2; ++i) {
                float posRand = 0F;
                float motionRand = 0.1F;//控制随机运动的，和粒子数量一起下次从cfg读
                this.world.addParticle(ParticleTypes.END_ROD, d0 - vec3d.x * 0.25D + posRand * (Math.random() - 0.5), d1 - vec3d.y * 0.25D + posRand * (Math.random() - 0.5), d2 - vec3d.z * 0.25D + posRand * (Math.random() - 0.5), vec3d.x + motionRand * (Math.random() - 0.5), vec3d.y + motionRand * (Math.random() - 0.5), vec3d.z + motionRand * (Math.random() - 0.5));
            }
        }
        else {
            /*
            for(PlayerEntity p : world.getPlayers()){
                p.sendMessage(new StringTextComponent("ticking spell" + this.getPosX() + "," + this.getPosY() + "," + this.getPosZ()));
            }//测试
             */

            int ticksAlive = this.dataManager.get(TICKS_ALIVE);
            this.dataManager.set(TICKS_ALIVE, ticksAlive + 1);
            if(ticksAlive > maxTimer){
                double mp = this.spellBallMPStorage.getMana();
                if(mp < 0.01D * this.spellBallMPStorage.getMaxMana()){
                    mp = 0D;
                }
                else{
                    mp = mp *(1.0D - descendingRate);
                }
                this.spellBallMPStorage.setMana(mp);
            }
            //指数降低mp存量

            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, true, ticksAlive >= 25, this.shootingEntity, RayTraceContext.BlockMode.COLLIDER);
            //这个includeShooter大概率是指在什么情况下允许射出投掷物的实体被投掷物击中，但是逻辑比较复杂，我读不太懂。此处照抄了DamagingProjectileEntity的逻辑
            /*
            for(PlayerEntity p : world.getPlayers()){
                p.sendMessage(new StringTextComponent(raytraceresult.getType().toString()));
            }//测试
             */
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            //if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                /*
                for(PlayerEntity p : world.getPlayers()){
                    p.sendMessage(new StringTextComponent("on impact" + this.getPosX() + "," + this.getPosY() + "," + this.getPosZ()));
                }//测试
                 */
                this.onImpact(raytraceresult);
            }

            Vec3d vec3d = this.getMotion();
            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + vec3d.y;//底面坐标
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
            
            //随时间而增加的能量消耗写在这


            if (this.spellBallMPStorage.getMana() == 0D){
                /*
                for(PlayerEntity p : world.getPlayers()){
                    p.sendMessage(new StringTextComponent("delete spell"));
                }//测试
                */
                this.remove();
            }

            this.translatedSpellProvider.getCompiled(source).executeOnHold(source);
        }
    }

    /*
    获取运动方向，如果运动方向和坐标轴不对齐返回null
    一般来说，方向和坐标轴对齐意味着这个法球是由发射器发出的
    public Direction isMotionAligned(){
        Vec3d v = this.getMotion();
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        if (y == 0){
            if (x == 0){
                if(z < 0)
                    return Direction.NORTH;
                else if(z > 0)
                    return Direction.SOUTH;
                else
                    return null;
            }
            else if (z == 0){
                if(x < 0)
                    return Direction.WEST;
                else
                    return Direction.EAST;
            }
            else
                return null;
        }
        else if (x == 0 && y == 0){
            if (y < 0)
                return Direction.DOWN;
            else
                return Direction.UP;
        }
        else
            return null;
    }
     */


    /**
    执行反射操作，传入镜子的两个方向属性
     */
    public void reflect(Direction face, BlockPos pos){
        //例如计算xz的反射，把xz和y的运算拆开
        //xz对换，y反向
        //写个计算出射位置的函数，或者直接用入射位置代替减少代码量
        double px = 0.5D + pos.getX();
        double py = 0.5D + pos.getY();
        double pz = 0.5D + pos.getZ();

        double x = this.getPosX() - px;
        double y = this.getPosY() - py + HALF_SIZE;
        double z = this.getPosZ() - pz;
        //法球中心的相对位置
        //请注意mc的位置指的是脚底位置，posY要+HALF_SIZE才是法球中心位置

        Vec3d v = this.getMotion();

        if (face == Direction.SOUTH){
            this.setPosition(px + y, py + x - HALF_SIZE, this.getPosZ());
            this.setMotion(-v.getY(), -v.getX(), v.getZ());
        }
        else if (face == Direction.NORTH){
            this.setPosition(px - y, py - x - HALF_SIZE, this.getPosZ());
            this.setMotion(v.getY(), v.getX(), v.getZ());
        }
        else if (face == Direction.EAST){
            this.setPosition(this.getPosX(), py + z - HALF_SIZE, pz + y);
            this.setMotion(v.getX(), -v.getZ(), -v.getY());
        }
        else if (face == Direction.WEST){
            this.setPosition(this.getPosX(), py - z - HALF_SIZE, pz - y);
            this.setMotion(v.getX(), v.getZ(), v.getY());
        }
        else if (face == Direction.UP){
            this.setPosition(px + z, this.getPosY(), pz + x);
            this.setMotion(-v.getZ(), v.getY(), -v.getX());
        }
        else if (face == Direction.DOWN){
            this.setPosition(px - z, this.getPosY(), pz - x);
            this.setMotion(v.getZ(), v.getY(), v.getX());
        }

    }

    /**
    执行拆分操作，传入镜子的方向属性
     */
    public void split(Direction face, BlockPos pos){
        this.spellBallMPStorage.setMana(this.spellBallMPStorage.getMana()/2);
        EntitySpellBall newSpell = this.clone();
        newSpell.reflect(face, pos);
        world.addEntity(newSpell);

        double px = 0.5D + pos.getX();
        double py = 0.5D + pos.getY();
        double pz = 0.5D + pos.getZ();

        double x = this.getPosX() - px;
        double y = this.getPosY() - py + HALF_SIZE;
        double z = this.getPosZ() - pz;

        this.setPosition(px - x, py - y, pz - z);
    }

    /**
     复制一个法球实体
     */
    public EntitySpellBall clone(){
        double x = this.getPosX();
        double y = this.getPosY();
        double z = this.getPosZ();
        Vec3d mot = this.getMotion();
        EntitySpellBall spell = new EntitySpellBall.Builder(world)
                .pos(x, y, z).motion(mot)
                .gravity(this.dataManager.get(GRAVITY))
                .shooter(this.shootingEntity)
                .setMP(this.spellBallMPStorage.getMana(), this.spellBallMPStorage.getMaxMana())
                .build();
        spell.translatedSpellProvider = this.translatedSpellProvider;
        spell.dataManager.set(TICKS_ALIVE, this.dataManager.get(TICKS_ALIVE));
        return spell;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side){
        if (cap == CapabilitySpell.SPELL_CAP){
            return LazyOptional.of(() -> this.translatedSpellProvider).cast();
        }
        else if (cap == CapabilityMPStorage.MP_STORAGE_CAP){
            return LazyOptional.of(() -> this.spellBallMPStorage).cast();
        }
        return super.getCapability(cap, side);
    }



}
