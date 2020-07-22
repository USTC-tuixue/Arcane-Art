package com.ustctuixue.arcaneart.automation.crystal;

import com.udojava.evalex.Expression;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;

import java.math.BigDecimal;

public class SolarCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public SolarCrystalTileEntity() {
        super(AutomationRegistry.SOLAR_CRYSTAL_TILEENTITY.get());
    }

    @Override
    public double crystalRegenRatio(){
        BigDecimal regenRatio = new Expression(AutomationConfig.Crystal.SOLAR_CRYSTAL_REGEN_RATIO.get()).with("x", new BigDecimal(world.getBiome(this.getPos()).getTemperature(this.getPos()))).eval();
        return regenRatio.doubleValue();
    }
}
