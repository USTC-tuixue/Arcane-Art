package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

import java.util.function.Supplier;

public class NewSpellEvent extends ServerLifecycleEvent
{
    public NewSpellEvent(MinecraftServer server)
    {
        super(server);
    }

    public void register(SpellKeyWord keyWord, Supplier<ISpell> spellSupplier)
    {
        Interpreter.SPELLS.put(keyWord, spellSupplier);
    }
}
