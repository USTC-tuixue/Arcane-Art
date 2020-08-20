package com.ustctuixue.arcaneart.gui;

import com.ustctuixue.arcaneart.gui.magicmenu.MagicMenu;
import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.misc.bookshelf.BookShelfScreen;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GUIEventHandler {
	@SubscribeEvent
	public static void onClientSetUpEvent(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ContainerTypeRegistry.magicContainer.get(), MagicMenu::new);
		ScreenManager.registerFactory(ContainerTypeRegistry.bookShelfContainer.get(), BookShelfScreen::new);
	}
}
