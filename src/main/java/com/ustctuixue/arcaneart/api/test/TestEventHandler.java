package com.ustctuixue.arcaneart.api.test;


import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;

public class TestEventHandler
{
    @SubscribeEvent
    public void onServerStart(@Nonnull FMLServerStartingEvent event)
    {
        TestCommands.register(event.getCommandDispatcher());

    }

    @SubscribeEvent
    public void onCommonSetup(FMLLoadCompleteEvent event)
    {
        SpellKeyWord.REGISTRY.getValues().forEach(e -> ArcaneArtAPI.LOGGER.debug(Spell.MARKER, e.getRegistryName()));
        SpellKeyWords.addAllTranslations();

    }
}
