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
    Hand hand;
    int switchedSpell;

    public static void handle(PacketSwitchSpell packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        PlayerEntity playerEntity = contextSupplier.get().getSender();
        if (playerEntity == null)
        {
            return;
        }
        contextSupplier.get().enqueueWork( () -> {
            ItemStack stack = playerEntity.getHeldItem(packet.hand);
            if (stack.getItem() instanceof ItemSpellCaster)
            {
                ((ItemSpellCaster) stack.getItem()).setSpellSlot(stack, packet.switchedSpell);
            }
        });
    }

    public static void encode(PacketSwitchSpell pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.hand);
        buf.writeVarInt(pkt.switchedSpell);
    }

    public static PacketSwitchSpell decode(PacketBuffer buf) {
        return new PacketSwitchSpell(buf.readEnumValue(Hand.class), buf.readVarInt());
    }
}
