package com.ustctuixue.arcaneart.gui;

import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.networking.KeyPack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
@Mod.EventBusSubscriber
public class testGUIM {
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		if(KeyLoader.showMagicMenu.isPressed()&&Minecraft.getInstance().isGameFocused()) {
		    KeyEvent.INSTANCE.sendToServer(new KeyPack("OpenMagicMenu"));
		}
	}
}
