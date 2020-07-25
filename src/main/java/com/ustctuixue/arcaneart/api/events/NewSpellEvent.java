package com.ustctuixue.arcaneart.api.events;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class NewSpellEvent extends ServerLifecycleEvent
{
    public NewSpellEvent(MinecraftServer server)
    {
        super(server);
    }
}
