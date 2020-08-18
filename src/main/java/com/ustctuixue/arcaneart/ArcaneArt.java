package com.ustctuixue.arcaneart;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.automation.AutomationRegistry;
import com.ustctuixue.arcaneart.misc.ContainerTypeRegistry;
import com.ustctuixue.arcaneart.gui.magicmenu.MagicMenu;
import com.ustctuixue.arcaneart.client.KeyLoader;
import com.ustctuixue.arcaneart.misc.bookshelf.BookShelfScreen;
import com.ustctuixue.arcaneart.networking.KeyEvent;
import com.ustctuixue.arcaneart.config.ArcaneArtConfig;
import com.ustctuixue.arcaneart.ritual.ritualMagic.RitualModuleRegistries;
import com.ustctuixue.arcaneart.spell.SpellModule;
import com.ustctuixue.arcaneart.ritual.RitualRegistries;
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

        SpellModuleRegistries.SpellKeyWords.SPELL_KEY_WORD_DEFERRED_REGISTER.register(modLoadingEventBus);


        modLoadingEventBus.addListener(this::commonSetup);
        modLoadingEventBus.addListener(this::clientSetup);


        MinecraftForge.EVENT_BUS.register(this);
        AutomationRegistry.BLOCK_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.ITEM_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AutomationRegistry.TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        com.ustctuixue.arcaneart.misc.Registry.register(FMLJavaModLoadingContext.get().getModEventBus());

        RitualRegistries.BLOCKS.register(modLoadingEventBus);
        RitualRegistries.ITEMS.register(modLoadingEventBus);
        RitualRegistries.TILE_ENTITIES.register(modLoadingEventBus);
        RitualModuleRegistries.RITUAL_DEFERRED_REGISTER.register(modLoadingEventBus);
        RitualModuleRegistries.ITEMS.register(modLoadingEventBus);
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
    }


    public static ItemGroup ARCANE_ART_ITEM_GROUP = new ItemGroup(ArcaneArt.MOD_NAME){
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.OBSIDIAN);
        }
    };
}
