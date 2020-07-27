package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.command.CommandLoader;
import com.ustctuixue.arcaneart.api.network.PacketHandler;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.impl.DefineVariableSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;

public class ServerLifecycleAPIEventHandler
{
    public static final PacketHandler packetHandler = new PacketHandler();

    @SubscribeEvent @SuppressWarnings("unused")
    public void onServerStart(@Nonnull FMLServerStartingEvent event)
    {
        packetHandler.initialize();
        LanguageManager.getInstance().readFromConfig();
        CommandLoader.registerAll(event.getCommandDispatcher());
        MinecraftForge.EVENT_BUS.post(new NewSpellEvent(event.getServer()));
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public void registerNewSpell(@Nonnull NewSpellEvent event)
    {
        event.register(SpellKeyWords.MAKE, DefineVariableSpell::new);
    }
}
