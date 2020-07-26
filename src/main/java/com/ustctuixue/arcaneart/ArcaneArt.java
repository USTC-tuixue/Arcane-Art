package com.ustctuixue.arcaneart;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.test.TestEventHandler;
import com.ustctuixue.arcaneart.api.test.TestObjects;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.gui.MagicMenu.MagicMenu;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.config.ArcaneArtConfig;
import com.ustctuixue.arcaneart.spell.SpellModule;
import com.ustctuixue.arcaneart.ritual.RitualRegistry;
import com.ustctuixue.arcaneart.spell.SpellModuleRegistries;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScreenManager;
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

import javax.annotation.Nonnull;

@Mod(ArcaneArt.MOD_ID)
public class ArcaneArt
{
    public static final String MOD_ID = "arcaneart";
    public static final String MOD_NAME = "Arcane Art";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static final Marker MAIN = MarkerManager.getMarker("MAIN");
    private static final Marker COMMON_SETUP = MarkerManager.getMarker("COMMON SETUP");
    public ArcaneArt()
    {
        LOGGER.info(MAIN, "Loading Mod " + MOD_NAME);
        ArcaneArtConfig.registerConfigs();

        IEventBus modLoadingEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ArcaneArtAPI().registerModule();
        new SpellModule().registerModule();

        modLoadingEventBus.register(new TestEventHandler());
        SpellModuleRegistries.SpellKeyWords.SPELL_KEY_WORD_DEFERRED_REGISTER.register(modLoadingEventBus);
        TestObjects.register();

        modLoadingEventBus.addListener(this::commonSetup);
        modLoadingEventBus.addListener(this::clientSetup);


        MinecraftForge.EVENT_BUS.register(this);

        ContainerTypeRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.BLOCK_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.ITEM_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());

        RitualRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RitualRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RitualRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Nonnull
    public static ResourceLocation getResourceLocation(@Nonnull String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info(COMMON_SETUP, "FML Common Setup Event");
    	KeyEvent.registerMessage();
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {
        KeyLoader.register();
        ScreenManager.registerFactory(ContainerTypeRegistry.magicContainer.get(), MagicMenu::new);
    }


    public static ItemGroup ARCANE_ART_ITEM_GROUP = new ItemGroup(ArcaneArt.MOD_NAME){
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.OBSIDIAN);
        }
    };
}
