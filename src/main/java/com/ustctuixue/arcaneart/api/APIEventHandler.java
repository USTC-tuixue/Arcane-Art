package com.ustctuixue.arcaneart.api;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.spell.*;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber(modid = ArcaneArt.MOD_ID)
public class APIEventHandler
{
    static final Logger LOGGER = LogManager.getLogger(ArcaneArt.MOD_NAME + " API");

    private static final Marker SETUP = MarkerManager.getMarker("SetUp");

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event)
    {
        CapabilityManager.INSTANCE.register(IManaBar.class, new CapabilityMP.Storage(), DefaultManaBar::new);
        CapabilityManager.INSTANCE.register(MPStorage.class, new CapabilityMPStorage.Storage(), MPStorage::new);
    }

    @SubscribeEvent
    public void attachAttribute(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity)
        {
            ((LivingEntity) entity).getAttributes().registerAttribute(CapabilityMP.MAX_MANA);
            ((LivingEntity) entity).getAttributes().registerAttribute(CapabilityMP.REGEN_RATE);
        }
    }



    @SubscribeEvent
    public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LivingEntity)
        {
            event.addCapability(
                    ArcaneArt.getResourceLocation("mp"),
                    new CapabilityMP.Provider()
            );
        }
    }

    @SubscribeEvent
    public void attachCapabilityItemStack(AttachCapabilitiesEvent<ItemStack> event)
    {
        Item item = event.getObject().getItem();
        if(item instanceof ItemSpellCaster || item instanceof ItemSpell)
        {
            event.addCapability(
                    ArcaneArt.getResourceLocation("spell"),
                    new CapabilitySpell.Provider()
            );
        }
    }

    @SubscribeEvent
    public void registerConfig(ModConfig.Reloading event)
    {
        ((CommentedFileConfig)event.getConfig().getConfigData()).load();
    }

    @SubscribeEvent
    public void createRegistry(RegistryEvent.NewRegistry event)
    {
        RegistryBuilder<SpellKeyWord> builder = new RegistryBuilder<>();
        SpellKeyWord.REGISTRY = builder.create();
    }

    @SubscribeEvent
    public void loadKeyWordMaps(FMLServerAboutToStartEvent event)
    {
        LanguageManager.getInstance().readFromConfig();
    }

    public void registerSpellKeyWords(RegistryEvent.Register<SpellKeyWord> event)
    {
        SpellKeyWords.registerAll(event.getRegistry());
    }

}
