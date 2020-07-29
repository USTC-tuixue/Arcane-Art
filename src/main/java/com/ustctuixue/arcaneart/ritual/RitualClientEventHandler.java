package com.ustctuixue.arcaneart.ritual;

import com.ustctuixue.arcaneart.ritual.device.DingTileEntityRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RitualClientEventHandler {

    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(RitualRegistry.dingCircleTileEntity.get(), DingTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RitualRegistry.dingSquareTileEntity.get(), DingTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RitualRegistry.dingCenterTileEntity.get(), DingTileEntityRenderer::new);
    }
}
