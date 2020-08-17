package com.ustctuixue.arcaneart.automation.luxtransport;

import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.api.spell.EntitySpellBall;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;

public class LuxEmitterTileentity extends TileEntity implements ITickableTileEntity {

    public LuxEmitterTileentity() {
        super(AutomationRegistry.LUX_EMITTER_TILEENTITY.get());
    }

    public static int TRANSF_COUNTER = 0;
    public double emitterTransferInterval = AutomationConfig.Emitter.EMIT_INTERVAL.get();//下次从cfg读
    public double emitterTransferAmount = AutomationConfig.Emitter.EMIT_AMOUNT.get();//下次从cfg读

    @Override
    public void tick() {
        if (this.world != null && !world.isRemote) {
            //这里是服务器逻辑

            for(Direction direction : Direction.values()){
                //任意面被充能终止工作
                if(this.getBlockState().getWeakPower(world, this.getPos(), direction) != 0){
                    TRANSF_COUNTER = 0;
                    return;
                }
            }

            if(TRANSF_COUNTER >= emitterTransferInterval){
                TileEntity mpStorageTE = world.getTileEntity(this.getPos().offset(
                        this.getBlockState().get(LuxEmitter.FACING).getOpposite()
                ));
                if(mpStorageTE != null){
                    LazyOptional<MPStorage> mpStorageCapLazyOptional = mpStorageTE.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
                    mpStorageCapLazyOptional.ifPresent((s) -> {
                        double MP = s.getMana();
                        if (MP >= emitterTransferAmount){
                            //必须有足够一次发射的能量才运作，抽取这些能量
                            s.setMana(MP - emitterTransferAmount);
                            //新建一个法球实体
                            world.addEntity(new EntitySpellBall.Builder(world).emitFromBlock(this.getPos(), this.getBlockState().get(LuxEmitter.FACING), AutomationConfig.Emitter.EMIT_SPEED.get()).setFullMP(emitterTransferAmount).build());
                        }
                    });
                }
            }
            else{
                ++TRANSF_COUNTER;
            }
        }

    }

}
