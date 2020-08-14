package com.ustctuixue.arcaneart.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import com.mojang.datafixers.types.templates.List;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class ShortcutPack implements IPacket<IClientPlayNetHandler> {
    private Inventory inner;

    public ShortcutPack(Inventory inv)
    {
    	this.inner=inv;
    }

	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
    	CompoundNBT tag=buf.readCompoundTag();
    	this.inner=new Inventory(9);
    	NonNullList<ItemStack> inner=NonNullList.<ItemStack>withSize(9,ItemStack.EMPTY);
    	ItemStackHelper.loadAllItems(tag, inner);
    	for(int i=0;i<9;i++) {
    		this.inner.setInventorySlotContents(i, inner.get(i));
    	}
	}

	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
    	CompoundNBT tag=new CompoundNBT();
    	NonNullList<ItemStack> inner=NonNullList.<ItemStack>withSize(9,ItemStack.EMPTY);
    	for(int i=0;i<9;i++)
    		inner.set(i,this.inner.getStackInSlot(i));
    		ItemStackHelper.saveAllItems(tag, inner);
    		buf.writeCompoundTag(tag);
		
	}

	@Override
	public void processPacket(IClientPlayNetHandler handler) {
    	LazyOptional<ISpellInventory> spellCap = Minecraft.getInstance().player
			.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
    		spellCap.ifPresent((cap)->{
    			for(int i=0;i<9;i++)
    			cap.writeShortcut(this.inner.getStackInSlot(i), i);
    		});
	}
}
