package com.ustctuixue.arcaneart;

import com.ustctuixue.arcaneart.api.APIEventHandler;
import com.ustctuixue.arcaneart.api.TestObjects;
import com.ustctuixue.arcaneart.api.client.APIClientEventHandler;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.config.ArcaneArtConfig;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
        LOGGER.info(MAIN, "Loading Mod " + MOD_NAME);
        ArcaneArtConfig.registerConfigs();
        MinecraftForge.EVENT_BUS.register(new APIEventHandler());
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new APIClientEventHandler());
        TestObjects.register();

        AutomationRegistry.BLOCK_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.ITEM_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {

    }

    public static ItemGroup ARCANE_ART_ITEM_GROUP = new ItemGroup(ArcaneArt.MOD_NAME){
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.OBSIDIAN);
        }
    };
}
