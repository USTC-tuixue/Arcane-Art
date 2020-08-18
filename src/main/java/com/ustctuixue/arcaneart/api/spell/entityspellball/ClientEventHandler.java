package com.ustctuixue.arcaneart.api.spell.entityspellball;

import com.ustctuixue.arcaneart.api.APIRegistries;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(APIRegistries.Entities.ENTITY_SPELL_REGISTER.get(), (EntityRendererManager manager) -> {
            return new EntitySpellBallRenderer(manager);
        });
    }
}
