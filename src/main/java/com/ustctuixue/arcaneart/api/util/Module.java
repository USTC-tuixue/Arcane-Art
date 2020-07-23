package com.ustctuixue.arcaneart.api.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public abstract class Module
{
    protected abstract Object[] getModLoadingEventHandler();
    protected abstract Object[] getCommonEventHandler();

    public void registerEventHandlers()
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
