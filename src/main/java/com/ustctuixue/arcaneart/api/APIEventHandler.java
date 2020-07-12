package com.ustctuixue.arcaneart.api;

import com.google.common.base.Suppliers;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.ManaBarImplementation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class APIEventHandler
{
    @CapabilityInject(IManaBar.class)
    public static Capability<IManaBar> MANA_BAR_CAP;

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event)
    {
        CapabilityManager.INSTANCE.register(IManaBar.class, new CapabilityMP.Storage(), ManaBarImplementation::new);
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

    public void regenMP(TickEvent.PlayerTickEvent event)
    {
        event.player.getCapability(MANA_BAR_CAP).ifPresent((manaBar)->{
            if (manaBar.coolDown())
            {

            }
        });
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LivingEntity)
        {
            event.addCapability(ArcaneArt.getResourceLocation("mp"), new ICapabilityProvider()
            {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
                {
                    return LazyOptional.of(new NonNullSupplier<T>()
                    {
                        @Nonnull
                        @Override
                        public T get()
                        {
                            return cap.getDefaultInstance();
                        }
                    });
                }
            });
        }
    }
}
