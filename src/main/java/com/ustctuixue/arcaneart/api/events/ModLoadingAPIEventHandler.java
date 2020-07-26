package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.APIRegistries;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;

public class ModLoadingAPIEventHandler
{
    private static final Marker SETUP = MarkerManager.getMarker("API_SETUP");

    @SubscribeEvent @SuppressWarnings("unused")
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


    @SubscribeEvent @SuppressWarnings("unused")
    public void registerSpellKeyWords(@Nonnull RegistryEvent.Register<SpellKeyWord> event)
    {
        SpellKeyWords.registerAll(event.getRegistry());
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public void registerEntityType(@Nonnull RegistryEvent.Register<EntityType<?>> event)
    {
        event.getRegistry().register(APIRegistries.Entities.SPELL_BALL_TYPE.setRegistryName(ArcaneArtAPI.getResourceLocation("spell_ball")));
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public void registerItems(@Nonnull RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(APIRegistries.Items.ITEM_SPELL.setRegistryName(ArcaneArtAPI.getResourceLocation("item_spell")));
    }

}
