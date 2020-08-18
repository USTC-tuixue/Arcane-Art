package com.ustctuixue.arcaneart.api.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public abstract class Module
{
    protected abstract Object[] getModLoadingEventHandler();
    protected abstract Object[] getCommonEventHandler();

    @OnlyIn(Dist.CLIENT)
    protected abstract Object[] getClientEventHandler();

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
        DistExecutor.callWhenOn(Dist.CLIENT, () -> this::getClientEventHandler);
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerClientHandlers()
    {
        for (int i = 0; i < getClientEventHandler().length; i++)
        {
            MinecraftForge.EVENT_BUS.register(getClientEventHandler()[i]);
        }
    }
}
