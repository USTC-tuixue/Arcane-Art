package com.ustctuixue.arcaneart.networking;

import java.util.function.Supplier;

import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.gui.magicmenu.MagicContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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
                NetworkHooks.openGui(player, new com.ustctuixue.arcaneart.gui.magicmenu.MagicMenuProvider());
            }
            if (message.split(":")[0].equals("ItchMagic") && player != null) {
				Container container = player.openContainer;
				if (container instanceof MagicContainer) {
					ItemStack book = container.inventorySlots.get(100).getStack();
					if (!book.isEmpty()) {
						ItemStack spell= Interpreter.fromWrittenBook(book);
						if(!spell.isEmpty()) {
						container.putStackInSlot(Integer.parseInt(message.split(":")[1]),spell);
						}
					}
				}
			}
        });
        ctx.get().setPacketHandled(true);
    }
}
