package com.ustctuixue.arcaneart.automation.luxtransport;

import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.spell.EntitySpellBall;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.automation.crystal.AbstractCollectiveCrystalTileEntity;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class LuxEmitterTileentity extends TileEntity implements ITickable {

    public LuxEmitterTileentity() {
        super(AutomationRegistry.LUX_EMITTER_TILEENTITY.get());
    }

    public static int TRANSF_COUNTER = 0;

    @Override
    public void tick() {
        if (this.world != null && !world.isRemote) {
            //这里是服务器逻辑
            if(TRANSF_COUNTER == emitterTransferInterval){
                TileEntity mpStorageTE = world.getTileEntity(this.getPos().offset(
                        this.getBlockState().get(LuxEmitter.FACING).getOpposite()
                ));
                if(mpStorageTE != null){
                    LazyOptional<MPStorage> mpStorageCapLazyOptional = mpStorageTE.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
                    mpStorageCapLazyOptional.ifPresent((s) -> {
                        double MP = s.getMana();
                        //新建一个法球实体
                        world.addEntity(new EntitySpellBall.Builder(world).pos(this.getPos()).build());
                    });
                }
            }
            else{
                ++TRANSF_COUNTER;
            }
        }

    }

    public double emitterTransferInterval = 20;//下次从cfg读
    public double emitterTransferAmount = 40;//下次从cfg读
}
