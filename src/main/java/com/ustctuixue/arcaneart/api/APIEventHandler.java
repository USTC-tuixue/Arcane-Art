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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber
public class APIEventHandler
{
    private static final Marker SETUP = MarkerManager.getMarker("SetUp");

    public APIEventHandler()
    {
        ArcaneArtAPI.LOGGER.info(SETUP, "New APIEventHandler");
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event)
    {
        ArcaneArtAPI.LOGGER.info(SETUP, "setup");
        CapabilityManager.INSTANCE.register(IManaBar.class, new CapabilityMP.Storage(), DefaultManaBar::new);
        CapabilityManager.INSTANCE.register(MPStorage.class, new CapabilityMPStorage.Storage(), MPStorage::new);
        CapabilityManager.INSTANCE.register(ISpellInventory.class, new Capability.IStorage<ISpellInventory>() {
			@Override
			public INBT writeNBT(Capability<ISpellInventory> capability, ISpellInventory instance,
					net.minecraft.util.Direction side) {return null;}

			@Override
			public void readNBT(Capability<ISpellInventory> capability, ISpellInventory instance,
					net.minecraft.util.Direction side, INBT nbt) {}
		}, SpellInventory::new);
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
		if (event.getObject() instanceof PlayerEntity) {
    			event.addCapability(ArcaneArt.getResourceLocation("spellinv"), new SpellInventoryProvider());
    		}
        }
    }
	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		LazyOptional<ISpellInventory> oldSpeedCap = event.getOriginal()
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
		LazyOptional<ISpellInventory> newSpeedCap = event.getPlayer()
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);

		if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
			newSpeedCap.ifPresent((newCap) -> {
				oldSpeedCap.ifPresent((oldCap) -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
				});
			});
		}
	}	
    @SubscribeEvent
    public void attachCapabilityItemStack(AttachCapabilitiesEvent<ItemStack> event)
    {
        Item item = event.getObject().getItem();
        if(item instanceof ItemSpell)
        {
            event.addCapability(
                    ArcaneArtAPI.getResourceLocation("spell"),
                    new CapabilitySpell.StorageProvider()
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
        ArcaneArtAPI.LOGGER.info(MarkerManager.getMarker("NewRegistry"), "Creating registry");
        RegistryBuilder<SpellKeyWord> builder = new RegistryBuilder<>();
        SpellKeyWord.REGISTRY = (ForgeRegistry<SpellKeyWord>) builder.setType(SpellKeyWord.class).setName(ArcaneArt.getResourceLocation("spell_words")).create();
    }


    @SubscribeEvent
    public void registerSpellKeyWords(RegistryEvent.Register<SpellKeyWord> event)
    {
        SpellKeyWords.registerAll(event.getRegistry());
    }

}
