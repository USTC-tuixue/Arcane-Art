package com.ustctuixue.arcaneart.automation.luxtransport;

import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.automation.crystal.AbstractCollectiveCrystalTileEntity;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class LuxEmitterTileentity extends TileEntity implements ITickable {

    public LuxEmitterTileentity() {
        super(AutomationRegistry.LUX_EMITTER_TILEENTITY.get());
    }

    @Override
    public void tick() {

    }
}
