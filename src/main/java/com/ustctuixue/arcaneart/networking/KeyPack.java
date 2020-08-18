package com.ustctuixue.arcaneart.networking;

import java.util.function.Supplier;

import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
import com.ustctuixue.arcaneart.gui.magicmenu.MagicContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

public class KeyPack
{
    private String message;
    private CompoundNBT additional;

    public KeyPack(PacketBuffer buffer)
    {
        this.message = buffer.readString(32767);
        this.additional = buffer.readCompoundTag();
    }

    public KeyPack(String message)
    {
        this.message = message;
        this.additional= new CompoundNBT();
    }

    public KeyPack(String message,CompoundNBT additional)
    {
        this.message = message;
        this.additional=additional;
    }
    public void toBytes(PacketBuffer buf)
    {
        buf.writeString(this.message);
        buf.writeCompoundTag(this.additional);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = ctx.get().getSender();
            if (message.equals("OpenMagicMenu") && player != null)
            {
                NetworkHooks.openGui(player, new com.ustctuixue.arcaneart.gui.magicmenu.MagicMenuProvider());
            }else
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
			}else
            if(message.equals("LoadSpellShortcut") && player != null) {
        		LazyOptional<ISpellInventory> spellCap = player
        				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
        		Inventory inventory = spellCap.orElseGet(SpellInventory::new).getShortCut();
        		CompoundNBT tag=new CompoundNBT();
            	NonNullList<ItemStack> inner=NonNullList.<ItemStack>withSize(9,ItemStack.EMPTY);
            	for(int i=0;i<9;i++)
            		inner.set(i,inventory.getStackInSlot(i));
            		ItemStackHelper.saveAllItems(tag, inner);
        		KeyEvent.INSTANCE.send(
                        PacketDistributor.PLAYER.with(
                                () -> {
                                    return player;
                                }
                        ),
                        new KeyPack("shortcut",tag));
            }else
            	if(message.equals("shortcut")) {
            	 	CompoundNBT tag=this.additional;
                	NonNullList<ItemStack> inner=NonNullList.<ItemStack>withSize(9,ItemStack.EMPTY);
                	ItemStackHelper.loadAllItems(tag, inner);
                	LazyOptional<ISpellInventory> spellCap = Minecraft.getInstance().player
                			.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
                	spellCap.ifPresent((cap)->{
            			for(int i=0;i<9;i++)
            			cap.writeShortcut(inner.get(i), i);
            		});
            	}
            	else if(message.split(":")[0].equals("Switch") && player != null) {
            		if(player.getHeldItemMainhand().getItem() instanceof ItemSpellCaster) {
            			ItemSpellCaster.setSpellSlot(player.getHeldItemMainhand(), Integer.parseInt(message.split(":")[1]));
            		}
            	}
        });
        ctx.get().setPacketHandled(true);
    }
}
