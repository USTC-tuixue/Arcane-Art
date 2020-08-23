package com.ustctuixue.arcaneart.gui.hud;

import com.ustctuixue.arcaneart.api.events.InGameAPIClientEventHandler;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.gui.hud.manabar.ManaBar;
import com.ustctuixue.arcaneart.gui.hud.shortcut.ShortcutHUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value=Dist.CLIENT)
public class HUDClientEvent {
	private static boolean render=false;
	@SubscribeEvent
	public static void onOverlayRender(RenderGameOverlayEvent event) {
		ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
		if (playerEntity == null) {
			return;
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES &&!render) {
			ManaBar manabar = new ManaBar(
					playerEntity.getCapability(CapabilityMP.MANA_BAR_CAP).orElseGet(DefaultManaBar::new).getMana()
							/ playerEntity.getAttribute(CapabilityMP.MAX_MANA).getValue());
			manabar.render();
			if(KeyLoader.shortcut.isKeyDown()&& Minecraft.getInstance().currentScreen==null &&Minecraft.getInstance().player.getHeldItemMainhand().getItem()  instanceof ItemSpellCaster) {
			ShortcutHUD shortcut=new ShortcutHUD(InGameAPIClientEventHandler.pointer);
			shortcut.render();
			render=true;
			}
		}
		if(event.getType()==RenderGameOverlayEvent.ElementType.ALL) {
			render=false;
		}
	}
}
