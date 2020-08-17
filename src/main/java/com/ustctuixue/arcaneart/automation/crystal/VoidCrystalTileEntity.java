package com.ustctuixue.arcaneart.automation.crystal;

import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import net.minecraft.block.BedrockBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

public class VoidCrystalTileEntity extends AbstractCollectiveCrystalTileEntity {
    public VoidCrystalTileEntity() {
        super(AutomationRegistry.VOID_CRYSTAL_TILEENTITY.get());
    }

    //咋判断是不是黑曜石啊
    @Override
    public double crystalRegenRatio(){
        //下方一块基岩，基岩下方接着3*3黑曜石才会工作
        BlockPos pos = this.getPos().down(1);
        assert world != null;
        if (!(world.getBlockState(pos).getBlock() instanceof BedrockBlock)){
            return 0.0D;
        }
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                BlockPos newpos = pos.add(i, -1, j);
                assert world != null;
                if (world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN)
                    return 0.0D;
            }
        }
        return AutomationConfig.Crystal.VOID_CRYSTAL_REGEN_RATIO.get() / 20;//从每秒恢复速率转换为每tick
    }
}
