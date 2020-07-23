package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.network.PacketSwitchSpell;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InGameAPIClientEventHandler
{
    @SubscribeEvent
    public void switchSpell(InputEvent.MouseScrollEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        ArcaneArt.LOGGER.debug("Mouse Scroll Delta: " + event.getScrollDelta());
        if (player == null) {
            return;
        }
        if (player.isSneaking() && player.getActiveItemStack().getItem() instanceof ItemSpellCaster)
        {
            ItemStack stack = player.getActiveItemStack();
            double delta = event.getScrollDelta();
            if (delta != 0) {

                int idx = ((ItemSpellCaster) stack.getItem()).getSpellSlot(stack);
                idx += event.getScrollDelta() > 0 ? 1 : -1;
                idx = idx < 0 ? idx + 9 : (idx >= 9 ? idx % 9 : idx);
                ServerLifecycleAPIEventHandler.packetHandler.getChannel().sendToServer(new PacketSwitchSpell(player.getActiveHand(), idx));
//                lastScrollTime = minecraft.world.getGameTime();
//                scrollDelta += delta;
//                int shift = (int) scrollDelta;
//                scrollDelta %= 1;
//                if (shift != 0) {
//                    RenderTickHandler.modeSwitchTimer = 100;
//                }
                event.setCanceled(true);
            }


        }
    }


}
