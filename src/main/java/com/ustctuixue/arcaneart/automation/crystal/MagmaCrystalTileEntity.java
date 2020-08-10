package com.ustctuixue.arcaneart.automation.crystal;

import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

public class MagmaCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public MagmaCrystalTileEntity() {
        super(AutomationRegistry.MAGMA_CRYSTAL_TILEENTITY.get());
    }

    @Override
    public void tick() {
        if (this.world != null && !world.isRemote) {
            //这里是服务器逻辑
            LazyOptional<MPStorage> mpStorageCapLazyOptional = this.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
            mpStorageCapLazyOptional.ifPresent((s) -> {
                //if (!world.canSeeSky(this.getPos()))
                    //return;
                //熔岩水晶不需要看到天空
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

    @Override
    public double crystalRegenRatio(){
        //下方5*5正方形去掉四个角的范围内均是岩浆，才会工作（药物配置范围）
        BlockPos pos = this.getPos().down(1);
        for(int i = -2; i <= 2; i++){
            //这里为了加速免去(-2, -2)的获取
            for(int j = -1; j <= 2; j++){
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
