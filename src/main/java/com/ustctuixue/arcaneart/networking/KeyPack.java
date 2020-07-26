package com.ustctuixue.arcaneart.networking;

import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.ustctuixue.arcaneart.gui.MagicMenu.MagicMenuProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ForgeConfig.Server;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class KeyPack
{
    private String message;

    public KeyPack(PacketBuffer buffer)
    {
        this.message = buffer.readString();
    }

    public KeyPack(String message)
    {
        this.message = message;
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeString(this.message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = ctx.get().getSender();
            if (message.equals("OpenMagicMenu") && player != null)
            {
                NetworkHooks.openGui(player, new com.ustctuixue.arcaneart.gui.MagicMenu.MagicMenuProvider());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
