package com.ustctuixue.arcaneart.gui.hud;

import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.gui.hud.manabar.ManaBar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HUDClientEvent {
	@SubscribeEvent
	public static void onOverlayRender(RenderGameOverlayEvent event) {
		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL){
			return;
		}
		ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
		if(playerEntity == null) {
			return;
		}
		ManaBar manabar = new ManaBar(
				playerEntity.getCapability(CapabilityMP.MANA_BAR_CAP).orElseGet(DefaultManaBar::new).getMana()
				/ playerEntity.getAttribute(CapabilityMP.MAX_MANA).getValue()
		);
		manabar.render();
	}
}
