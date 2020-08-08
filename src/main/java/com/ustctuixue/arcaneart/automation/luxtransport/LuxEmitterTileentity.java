package com.ustctuixue.arcaneart.automation.luxtransport;

import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.automation.crystal.AbstractCollectiveCrystalTileEntity;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;

public class LuxEmitterTileentity extends TileEntity implements ITickable {

    public LuxEmitterTileentity() {
        super(AutomationRegistry.LUX_EMITTER_TILEENTITY.get());
    }

    @Override
    public void tick() {
        if (this.world != null && !world.isRemote) {
            //这里是服务器逻辑
            LazyOptional<MPStorage> mpStorageCapLazyOptional = this.getPos().(/*获得面向的背面的方块的te*/).getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
            mpStorageCapLazyOptional.ifPresent((s) -> {

            });
        }
    }
}
