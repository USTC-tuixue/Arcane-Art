package com.ustctuixue.arcaneart.automation;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CollectiveCrystalTileEntity extends TileEntity implements ITickableTileEntity {
    public CollectiveCrystalTileEntity() {
        super(AutomationRegistry.COLLECTIVE_CRYSTAL_TILEENTITY.get());
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            //这里是服务器逻辑
        }
    }
}
