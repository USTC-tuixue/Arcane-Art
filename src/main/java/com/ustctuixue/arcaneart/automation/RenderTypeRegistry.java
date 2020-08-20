package com.ustctuixue.arcaneart.automation;


import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class RenderTypeRegistry {
    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(AutomationRegistry.LUX_REFLECTOR_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AutomationRegistry.LUX_SPLITTER_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AutomationRegistry.SOLAR_CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AutomationRegistry.VOID_CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AutomationRegistry.FROZEN_CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AutomationRegistry.MAGMA_CRYSTAL_BLOCK.get(), RenderType.getTranslucent());

    }
}
