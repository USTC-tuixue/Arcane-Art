package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractCollectiveCrystalTileEntity extends TileEntity implements ITickableTileEntity {
    public AbstractCollectiveCrystalTileEntity() {
        super(AutomationRegistry.COLLECTIVE_CRYSTAL_TILEENTITY.get());
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityMPStorage.MP_STORAGE_CAP) {
            return LazyOptional.of(() -> {
                return new MPStorage();
            }).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            //这里是服务器逻辑
            LazyOptional<MPStorage> MPStorageCapLazyOptional = this.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
            MPStorageCapLazyOptional.ifPresent((s) -> {
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
