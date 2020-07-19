package com.ustctuixue.arcaneart.api.test;


import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;

public class TestEventHandler
{
    @SubscribeEvent
    public void onServerStart(@Nonnull FMLServerStartingEvent event)
    {
        TestCommands.register(event.getCommandDispatcher());
    }
}
