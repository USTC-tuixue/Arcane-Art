package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class SpellDispatcher extends CommandDispatcher<SpellCasterSource>
{
    static SpellDispatcher DISPATCHER = new SpellDispatcher();

    /**
     * Check if spell get any errors
     * @param spell spell to check
     * @param source spell caster source
     * @return true for no errors
     */
    public boolean checkSpell(String spell, SpellCasterSource source)
    {
        return parse(spell, source).getExceptions().size() == 0;
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
}
