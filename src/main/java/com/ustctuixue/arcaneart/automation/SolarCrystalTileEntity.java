package com.ustctuixue.arcaneart.automation;

import com.udojava.evalex.Expression;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.math.BigDecimal;

public class SolarCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public SolarCrystalTileEntity(TileEntityType<? extends AbstractCollectiveCrystalTileEntity> entityType)
    {
        super(entityType);
    }

    @Override
    public double crystalRegenRatio(){
        BigDecimal regenRatio = new Expression(AutomationConfig.Crystal.SOLAR_CRYSTAL_REGEN_RATIO.get()).with("x", new BigDecimal(world.getBiome(this.getPos()).getTemperature(this.getPos()))).eval();
        return regenRatio.doubleValue();
    }
}
