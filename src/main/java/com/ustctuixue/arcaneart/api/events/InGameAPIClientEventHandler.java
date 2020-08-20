package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.networking.KeyPack;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value=Dist.CLIENT)
public class InGameAPIClientEventHandler {
	public static int pointer = -1;

	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void switchSpell(InputEvent.MouseScrollEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (KeyLoader.shortcut.isKeyDown() && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemSpellCaster) {
			ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
			double delta = event.getScrollDelta();
			if (delta != 0) {
				pointer += event.getScrollDelta() > 0 ? -1 : 1;
				pointer = pointer < 0 ? pointer + 9 : (pointer >= 9 ? pointer % 9 : pointer);
				event.setCanceled(true);
			}
		}
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onKeyDown(KeyInputEvent event) {
		if (KeyLoader.shortcut.isPressed() && Minecraft.getInstance().currentScreen == null) {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ItemSpellCaster) {
				if (pointer == -1) {
					pointer = ItemSpellCaster.getSpellSlot(Minecraft.getInstance().player.getHeldItemMainhand());
				}
			}
		}
		if (event.getAction() == 0 && event.getKey() == KeyLoader.shortcut.getKey().getKeyCode()) {
			if (Minecraft.getInstance().player!=null && Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ItemSpellCaster ) {
				KeyEvent.INSTANCE.sendToServer(new KeyPack("Switch:"+ pointer));
				pointer=-1;
			}
		}
	}
	
	@SubscribeEvent
    @SuppressWarnings("unused")
    public void renderCasterTooltip(ItemTooltipEvent event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        if (player != null && stack.getItem() instanceof ItemSpellCaster)
        {
            ITranslatedSpellProvider provider = ItemSpellCaster.getSpellProvider(player, ItemSpellCaster.getSpellSlot(stack));
            stack.setDisplayName(new StringTextComponent(
                    provider.getSpell().getName()
            ));
        }
    }

    @SuppressWarnings("unused")
	@SubscribeEvent
	public static void onPlayerLogIn(LoggedInEvent event) {
		KeyEvent.INSTANCE.sendToServer(new KeyPack("LoadSpellShortcut"));
	}
}
