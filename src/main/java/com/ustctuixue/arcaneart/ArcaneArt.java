package com.ustctuixue.arcaneart;

import com.ustctuixue.arcaneart.api.APIEventHandler;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.gui.MagicMenu.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.gui.MagicMenu.MagicContainer;
import com.ustctuixue.arcaneart.gui.MagicMenu.MagicMenu;
import com.ustctuixue.arcaneart.item.ItemRegistry;
import com.ustctuixue.arcaneart.networking.KeyEvent;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;

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
    	ContainerTypeRegistry.CONTAINERS.register(eventBus);
        AutomationRegistry.BLOCK_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.ITEM_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
    	KeyEvent.registerMessage();
    }
    public void clientSetup(final FMLClientSetupEvent event)
    {
    	KeyLoader.register();
        ScreenManager.registerFactory(ContainerTypeRegistry.magicContainer.get(), (MagicContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) -> {
            return new MagicMenu(screenContainer,inv,titleIn);
        });
    }

    public static ItemGroup ARCANE_ART_ITEM_GROUP = new ItemGroup("arcaneart_item_group"){
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.OBSIDIAN);
        }
    };
}
