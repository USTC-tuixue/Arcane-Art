package com.ustctuixue.arcaneart.api.client;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.network.PacketSwitchSpell;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientEventHandler
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void switchSpell(InputEvent.MouseScrollEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.isSneaking() && player.getActiveItemStack().getItem() instanceof ItemSpellCaster)
        {
            ItemStack stack = player.getActiveItemStack();
            int idx = ((ItemSpellCaster) stack.getItem()).getSpellSlot(stack);
            idx += event.getScrollDelta() > 0 ? 1 : event.getScrollDelta() < 0 ? -1 : 0;
            ArcaneArtAPI.packetHandler.getChannel().sendToServer(new PacketSwitchSpell(player.getActiveHand(), idx));
        }
    }


}
