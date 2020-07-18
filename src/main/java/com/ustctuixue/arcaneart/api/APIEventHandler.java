package com.ustctuixue.arcaneart.api;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber(modid = ArcaneArt.MOD_ID)
public class APIEventHandler
{
    static final Logger LOGGER = LogManager.getLogger(ArcaneArt.MOD_NAME + " API");

    private static final Marker MAGIC_REGEN = MarkerManager.getMarker("Magic Regen");
    private static final Marker SETUP = MarkerManager.getMarker("SetUp");

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event)
    {
        CapabilityManager.INSTANCE.register(IManaBar.class, new CapabilityMP.Storage(), DefaultManaBar::new);
        CapabilityManager.INSTANCE.register(MPStorage.class, new CapabilityMPStorage.Storage(), MPStorage::new);
        CapabilityManager.INSTANCE.register(
                  ISpellInventory.class,
        		  new Capability.IStorage<ISpellInventory>() {
        		    @Override
        		    public INBT writeNBT(Capability<ISpellInventory> capability, ISpellInventory instance, net.minecraft.util.Direction side) {
        		      return instance.serializeNBT();
        		    }

        		    @Override
        		    public void readNBT(Capability<ISpellInventory> capability, ISpellInventory instance, net.minecraft.util.Direction side, INBT nbt) {
        		    	instance.deserializeNBT((CompoundNBT)nbt);
        		    }
        		  },
        		  () -> null
        		);
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
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LivingEntity)
        {
            event.addCapability(
                    ArcaneArt.getResourceLocation("mp"),
                    new CapabilityMP.Provider()
            );
            if (event.getObject() instanceof PlayerEntity) {
                event.addCapability(ArcaneArt.getResourceLocation("spellinventory"),new SpellInventoryProvider());
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            LazyOptional<ISpellInventory> oldSpeedCap = event.getOriginal().getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
            LazyOptional<ISpellInventory> newSpeedCap = event.getPlayer().getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
            if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
                newSpeedCap.ifPresent((newCap) -> {
                    oldSpeedCap.ifPresent((oldCap) -> {
                        newCap.deserializeNBT(oldCap.serializeNBT());
                    });
                });
            }
        }
    }
    @SubscribeEvent
    public void registerConfig(ModConfig.Reloading event)
    {
        ((CommentedFileConfig)event.getConfig().getConfigData()).load();
    }
}