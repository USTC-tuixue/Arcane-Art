package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.network.PacketSwitchSpell;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InGameAPIClientEventHandler
{
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void switchSpell(InputEvent.MouseScrollEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.isSneaking() && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemSpellCaster)
        {
            ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
            double delta = event.getScrollDelta();
            if (delta != 0) {
                int idx = ((ItemSpellCaster) stack.getItem()).getSpellSlot(stack);
                idx += event.getScrollDelta() > 0 ? 1 : -1;
                idx = idx < 0 ? idx + 9 : (idx >= 9 ? idx % 9 : idx);
                ServerLifecycleAPIEventHandler.packetHandler.getChannel().sendToServer(new PacketSwitchSpell(idx));
                event.setCanceled(true);
            }
        }
    }


}
