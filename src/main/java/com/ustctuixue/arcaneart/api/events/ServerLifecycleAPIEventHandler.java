package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.command.CommandLoader;
import com.ustctuixue.arcaneart.api.network.PacketHandler;
import com.ustctuixue.arcaneart.api.spell.Spells;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;

public class ServerLifecycleAPIEventHandler
{
    public static final PacketHandler packetHandler = new PacketHandler();

    @SubscribeEvent
    public void onServerStart(@Nonnull FMLServerStartingEvent event)
    {
        packetHandler.initialize();
        LanguageManager.getInstance().readFromConfig();
        CommandLoader.registerAll(event.getCommandDispatcher());
        MinecraftForge.EVENT_BUS.post(new SpellDispatcher.NewSpellEvent(event.getServer()));
    }

    @SubscribeEvent
    public void registerNewSpell(@Nonnull SpellDispatcher.NewSpellEvent event)
    {
        Spells.registerAll(event.getDispatcher());
    }
}
