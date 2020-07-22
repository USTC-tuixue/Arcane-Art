package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//右键方块显示所在位置温度的调试棒
//right click to give out the temperature, in Kelvin
public class TemperatureDebugStick extends Item {
    public TemperatureDebugStick() {
        super(new Item.Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        double temperature = EnvironmentHelper.getTemperature(world, blockpos);
        StringTextComponent stringTextComponent = new StringTextComponent("T = "+String.valueOf(temperature)+"K");
        context.getPlayer().sendMessage(stringTextComponent);
        return ActionResultType.SUCCESS;
    }
}