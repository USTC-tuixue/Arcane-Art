package com.ustctuixue.arcaneart.api.events;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.mp.*;
import com.ustctuixue.arcaneart.api.mp.tile.*;
import com.ustctuixue.arcaneart.api.spell.*;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryProvider;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class InGameAPIEventHandler
{

    @SubscribeEvent
    public void attachAttribute(@Nonnull EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity)
        {
            ((LivingEntity) entity).getAttributes().registerAttribute(CapabilityMP.MAX_MANA);
            ((LivingEntity) entity).getAttributes().registerAttribute(CapabilityMP.REGEN_RATE);
            ((LivingEntity) entity).getAttributes().registerAttribute(SpellCasterTiers.CASTER_TIER);
        }
    }




    @SubscribeEvent
    public void attachCapabilityEntity(@Nonnull AttachCapabilitiesEvent<Entity> event)
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
	public static void onPlayerCloned(@Nonnull PlayerEvent.Clone event) {
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
    public void attachCapabilityItemStack(@Nonnull AttachCapabilitiesEvent<ItemStack> event)
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
    public void registerConfig(@Nonnull ModConfig.Reloading event)
    {
        ((CommentedFileConfig)event.getConfig().getConfigData()).load();
    }




}
