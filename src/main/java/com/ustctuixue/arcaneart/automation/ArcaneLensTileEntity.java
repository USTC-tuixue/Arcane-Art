package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.automation.crystal.AbstractCollectiveCrystalTileEntity;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ArcaneLensTileEntity extends TileEntity implements ITickable {

    public ArcaneLensTileEntity(TileEntityType<? extends ArcaneLensTileEntity> entityType) {
        super(entityType);
    }

    @Override
    public void tick() {

    }
}
