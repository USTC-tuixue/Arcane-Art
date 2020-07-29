package com.ustctuixue.arcaneart.gui.magicmenu;

import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.networking.KeyPack;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GUIKeyHandler
{
    @SubscribeEvent
    public static void onKeyInput(KeyInputEvent event)
    {
        if (KeyLoader.showMagicMenu.isPressed() && Minecraft.getInstance().isGameFocused())
        {
            KeyEvent.INSTANCE.sendToServer(new KeyPack("OpenMagicMenu"));
        }
    }
}
