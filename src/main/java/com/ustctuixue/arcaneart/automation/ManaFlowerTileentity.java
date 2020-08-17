package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.api.spell.CapabilitySpell;
import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.automation.crystal.AbstractCollectiveCrystalTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.util.Direction.byHorizontalIndex;

public class ManaFlowerTileentity extends TileEntity implements ITickableTileEntity {
    public ManaFlowerTileentity() {
        super(AutomationRegistry.MANA_FLOWER_TILEENTITY.get());
    }

    public static int tier = 10;

    public ITranslatedSpellProvider translatedSpellProvider = new ITranslatedSpellProvider.Impl();
    public MPStorage FlowerMPStorage = createMPStorage();

    @OnlyIn(Dist.DEDICATED_SERVER)
    private final SpellCasterSource source = createSource();

    @OnlyIn(Dist.DEDICATED_SERVER)
    private SpellCasterSource createSource(){
        assert world != null;
        Direction face = world.getBlockState(this.getPos()).get(ManaFlower.FACING);
        float pitch = 0;
        switch(face){
            case SOUTH:
                pitch = 0;
                break;
            case WEST:
                pitch = 90;
                break;
            case NORTH:
                pitch = 180;
                break;
            case EAST:
                pitch = 270;
                break;
        }
        return new SpellCasterSource(
                new Vec3d(this.getPos()), new Vec2f(pitch, 0),
                (ServerWorld) this.world,
                this.world.getServer(), null, this.FlowerMPStorage, tier
        );
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilitySpell.SPELL_CAP){
            return LazyOptional.of(() -> this.translatedSpellProvider).cast();
        }
        if (cap == CapabilityMPStorage.MP_STORAGE_CAP){
            return LazyOptional.of(() -> this.FlowerMPStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    private MPStorage createMPStorage(){
        MPStorage mps = new MPStorage();
        mps.setMaxMana(100.0D);
        //mps.setMaxMana(AutomationConfig.Crystal.CRYSTAL_MAX_MP.get());
        //mps.setOutputRateLimit(AutomationConfig.Crystal.CRYSTAL_MAX_OUTPUT.get());
        //mps.setInputRateLimit(0.0D);
        return mps;
    }

    @Override
    public void tick() {
        if (this.world != null && !world.isRemote) {
            //这里是服务器逻辑
            LazyOptional<MPStorage> mpStorageCapLazyOptional = this.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
            mpStorageCapLazyOptional.ifPresent((s) -> {
                if (this.translatedSpellProvider.hasSpell()){
                    this.translatedSpellProvider.getCompiled(source).executeOnHold(source);
                }
            });
        }
    }
}
