package com.ustctuixue.arcaneart;

import com.ustctuixue.arcaneart.api.APIEventHandler;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.item.ItemRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(ArcaneArt.MOD_ID)
public class ArcaneArt
{
    public static final String MOD_ID = "arcaneart";
    public static final String MOD_NAME = "Arcane Art";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static final Marker MAIN = MarkerManager.getMarker("MAIN");
    public ArcaneArt()
    {
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info(MAIN, "Loading Mod " + MOD_NAME);
        MinecraftForge.EVENT_BUS.register(new APIEventHandler());
        MinecraftForge.EVENT_BUS.register(this);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
    	ItemRegistry.ITEMS.register(eventBus);
    }

    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    public void clientSetup(final FMLClientSetupEvent event)
    {
    	KeyLoader.register();
    }
}