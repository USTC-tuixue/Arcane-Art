package com.ustctuixue.arcaneart.client;

import java.awt.event.KeyEvent;

import org.lwjgl.glfw.GLFW;

import net.java.games.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardCharTypedEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyLoader {
	public static final String CONFIG_SHOW_MENU = "key.arcaneart.showmagicmenu";
	public static final String CONFIG_SHORTCUT = "key.arcaneart.shortcut";
	public static final String CONFIG_CATE = "key.categories.arcaneart";
	public static KeyBinding showMagicMenu;
	public static KeyBinding shortcut;
	public static void register() {
	    IKeyConflictContext ctx = new IKeyConflictContext() {
	        @Override
	        public boolean isActive() {
	          return true;
	        }

	        @Override
	        public boolean conflicts(IKeyConflictContext other) {
	          return false;
	        }
	      };
	      showMagicMenu = new KeyBinding(CONFIG_SHOW_MENU, GLFW.GLFW_KEY_H, CONFIG_CATE);
	      shortcut = new KeyBinding(CONFIG_SHORTCUT, GLFW.GLFW_KEY_N, CONFIG_CATE);
	      showMagicMenu.setKeyConflictContext(ctx);
	      shortcut.setKeyConflictContext(ctx);
	      ClientRegistry.registerKeyBinding(showMagicMenu);
	      ClientRegistry.registerKeyBinding(shortcut);
	}
}
