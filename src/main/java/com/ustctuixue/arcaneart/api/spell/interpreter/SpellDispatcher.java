package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

import java.util.List;

public class SpellDispatcher extends CommandDispatcher<SpellCasterSource>
{
    static SpellDispatcher DISPATCHER = new SpellDispatcher();

    /**
     * Check if spell get any errors
     * @param spell spell to check
     * @param source spell caster source
     * @return true for no errors
     */
    public boolean checkSpellSentence(String spell, SpellCasterSource source)
    {
        return parse(spell, source).getExceptions().size() == 0;
    }

    public static boolean checkSpell(TranslatedSpell spell, SpellCasterSource source)
    {
        final boolean[] f = {true};
        spell.getCommonSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        spell.getOnHoldSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        spell.getOnReleaseSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        return f[0];
    }

    public static class NewSpellEvent extends ServerLifecycleEvent
    {
        public NewSpellEvent(MinecraftServer server)
        {
            super(server);
        }

        public SpellDispatcher getDispatcher()
        {
            return DISPATCHER;
        }
    }

    public static int executeSpell(String spell, SpellCasterSource source)
    {
        try
        {
            return DISPATCHER.execute(DISPATCHER.parse(spell, source));
        }catch (CommandSyntaxException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static int executeSpell(List<String> spells, SpellCasterSource source)
    {
        boolean f = true;
        for (String spell : spells)
        {
            f = f &&  executeSpell(spell, source) != 0;
        }
        return f ? 1 : 0;
    }
}
