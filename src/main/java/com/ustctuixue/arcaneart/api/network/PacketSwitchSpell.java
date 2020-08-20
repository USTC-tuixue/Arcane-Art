package com.ustctuixue.arcaneart.api.network;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@AllArgsConstructor
public class PacketSwitchSpell
{
    int switchedSpell;

    static void handle(PacketSwitchSpell packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        ArcaneArtAPI.LOGGER.debug("Received spell switch packet, handling...");
        PlayerEntity playerEntity = contextSupplier.get().getSender();
        if (playerEntity == null)
        {
            return;
        }
        contextSupplier.get().enqueueWork( () -> {
            ItemStack stack = playerEntity.getHeldItem(Hand.MAIN_HAND);
            if (stack.getItem() instanceof ItemSpellCaster)
            {
                ItemSpellCaster.setSpellSlot(stack, packet.switchedSpell);
            }
        });
    }

    static void encode(PacketSwitchSpell pkt, PacketBuffer buf) {
        buf.writeVarInt(pkt.switchedSpell);
    }

    static PacketSwitchSpell decode(PacketBuffer buf) {
        return new PacketSwitchSpell(buf.readVarInt());
    }
}
