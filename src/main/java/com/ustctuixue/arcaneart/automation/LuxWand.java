package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//右键方块显示所在位置温度的调试棒
//right click to give out the temperature, in Kelvin
public class LuxWand extends Item {
    public LuxWand() {
        super(new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState block = world.getBlockState(blockpos);
        AtomicReference<StringTextComponent> stringTextComponent = null;
        boolean flag = false; //true代表法杖已经起效过，不需要再显示温度
        if (block.hasTileEntity()) {
            TileEntity te = world.getTileEntity(blockpos);
            assert te != null;
            LazyOptional<MPStorage> mpStorageCapLazyOptional = te.getCapability(CapabilityMPStorage.MP_STORAGE_CAP);
            mpStorageCapLazyOptional.ifPresent((s) -> {
                double mana = s.getMana();
                double maxMP = s.getMaxMP();
                stringTextComponent.set(new StringTextComponent("Current MP: " + mana + " / Max MP: " + maxMP));
            });
            if(mpStorageCapLazyOptional.isPresent())
                flag = true;

        }
        if (!flag){
            double temperature = EnvHelper.getTemperature(world, blockpos);
            stringTextComponent.set(new StringTextComponent("Temperature: " + temperature + "K"));
        }
        context.getPlayer().sendMessage(stringTextComponent.get());
        return ActionResultType.SUCCESS;
    }
}