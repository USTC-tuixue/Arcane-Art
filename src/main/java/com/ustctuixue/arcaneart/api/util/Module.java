package com.ustctuixue.arcaneart.api.util;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public abstract class Module
{
    protected abstract Object[] getModLoadingEventHandler();
    protected abstract Object[] getCommonEventHandler();


    public void registerModule()
    {
        IEventBus loadingBus = FMLJavaModLoadingContext.get().getModEventBus();
        for (int i = 0; i < getModLoadingEventHandler().length; i++)
        {
            loadingBus.register(getModLoadingEventHandler()[i]);
        }
        for (int i = 0; i < getCommonEventHandler().length; i++)
        {
            MinecraftForge.EVENT_BUS.register(getCommonEventHandler()[i]);
        }
    }
}
