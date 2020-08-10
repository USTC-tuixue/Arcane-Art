package com.ustctuixue.arcaneart.automation.crystal;

import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractCollectiveCrystalTileEntity extends TileEntity implements ITickableTileEntity {

    public AbstractCollectiveCrystalTileEntity(TileEntityType<? extends AbstractCollectiveCrystalTileEntity> entityType) {
        super(entityType);
    }

    public MPStorage CrystalMPStorage = createMPStorage();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityMPStorage.MP_STORAGE_CAP){
            return LazyOptional.of(() -> this.CrystalMPStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    private MPStorage createMPStorage(){
        MPStorage mps = new MPStorage();
        mps.setMaxMP(AutomationConfig.Crystal.CRYSTAL_MAX_MP.get());
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
                if (!world.canSeeSky(this.getPos()))
                    return;//默认情况下水晶需要看到天空以工作
                double regenRatio = crystalRegenRatio();
                if (regenRatio == 0)
                    return;
                double MP = s.getMana();
                double maxMP = s.getMaxMP();
                MP += regenRatio;
                if (MP >= maxMP)
                    MP = maxMP;
                s.setMana(MP);
                //按回复速率产生MP
            });
        }
    }

    public double crystalRegenRatio(){
        return 0.0D;
    }
}
