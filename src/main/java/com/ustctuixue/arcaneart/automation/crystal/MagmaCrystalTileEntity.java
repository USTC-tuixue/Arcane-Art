package com.ustctuixue.arcaneart.automation.crystal;

import com.udojava.evalex.Expression;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.automation.EnvHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

import java.math.BigDecimal;

public class MagmaCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public MagmaCrystalTileEntity() {
        super(AutomationRegistry.MAGMA_CRYSTAL_TILEENTITY.get());
    }

    @Override
    public double crystalRegenRatio(){
        //下方5*5正方形去掉四个角的范围内均是岩浆，才会工作（药物配置范围）
        BlockPos pos = this.getPos().down(1);
        for(int i = -2; i <= 2; i++){
            //这里为了加速免去(-2, -2)的获取
            for(int j = -2; j <= 2; j++){
                BlockPos newpos = pos.add(i, 0, j);
                assert world != null;
                if (((i == -2)||(i == 2))&&((j == -2)||(j == 2)))
                    continue;
                if (world.getBlockState(newpos).getMaterial() != Material.LAVA)
                    return 0.0D;
            }
        }
        return AutomationConfig.Crystal.MAGMA_CRYSTAL_REGEN_RATIO.get() / 20;//从每秒恢复速率转换为每tick
    }
}
