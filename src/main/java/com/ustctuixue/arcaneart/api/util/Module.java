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

    private List<DeferredRegister<?>> deferredRegisters = Lists.newArrayList();

    public <T extends IForgeRegistryEntry<T>> DeferredRegister<T> getDeferredRegister(IForgeRegistry<T> registry, String modid)
    {
        DeferredRegister<T> result = new DeferredRegister<>(registry, modid);
        deferredRegisters.add(result);
        return result;
    }

    public void registerModule()
    {
        IEventBus loadingBus = FMLJavaModLoadingContext.get().getModEventBus();
        for (int i = 0; i < getModLoadingEventHandler().length; i++)
        {
            loadingBus.register(getModLoadingEventHandler()[i]);
        }
        deferredRegisters.forEach(deferredRegister -> deferredRegister.register(loadingBus));
        for (int i = 0; i < getCommonEventHandler().length; i++)
        {
            MinecraftForge.EVENT_BUS.register(getCommonEventHandler()[i]);
        }
    }
}
