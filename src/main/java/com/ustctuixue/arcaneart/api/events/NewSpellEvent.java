package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

public class NewSpellEvent extends ServerLifecycleEvent
{
    public static Marker REGISTER_SPELL = MarkerManager.getMarker("REGISTER SPELL");
    public NewSpellEvent(MinecraftServer server)
    {
        super(server);
    }

    public void register(SpellKeyWord keyWord, Supplier<ISpell> spellSupplier)
    {
        ArcaneArtAPI.LOGGER.info(REGISTER_SPELL, "Registering spell for spell keyword: " + keyWord);
        Interpreter.SPELLS.put(keyWord, spellSupplier);
    }
}
