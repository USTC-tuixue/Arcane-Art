package com.ustctuixue.arcaneart.automation.crystal;

import com.udojava.evalex.Expression;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.automation.EnvHelper;

import java.math.BigDecimal;

public class FrozenCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public FrozenCrystalTileEntity() {
        super(AutomationRegistry.FROZEN_CRYSTAL_TILEENTITY.get());
    }

    @Override
    public double crystalRegenRatio(){
        BigDecimal regenRatio = new Expression(AutomationConfig.Crystal.FROZEN_CRYSTAL_REGEN_RATIO.get()).with("T", new BigDecimal(EnvHelper.getTemperature(world, this.pos))).eval();
        return (regenRatio.doubleValue())/20;//从每秒恢复速率转换为每tick
    }
}
