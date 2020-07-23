package com.ustctuixue.arcaneart.gui.hud;

import com.ustctuixue.arcaneart.gui.hud.Manabar.ManaBar;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HUDClientEvent {
@SubscribeEvent
public static void onOverlayRender(RenderGameOverlayEvent event) {
	if(event.getType()!=RenderGameOverlayEvent.ElementType.ALL){
		return;
	}
	if(Minecraft.getInstance().player==null) {
		return;
	}
	ManaBar manabar=new ManaBar(0.91);
	manabar.render();
}
}
