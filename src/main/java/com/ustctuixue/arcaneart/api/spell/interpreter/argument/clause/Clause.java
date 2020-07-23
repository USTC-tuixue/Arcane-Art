package com.ustctuixue.arcaneart.api.spell.interpreter.argument.clause;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.ArgumentUtil;


public abstract class Clause<T> implements ArgumentType<T>
{
    protected abstract SpellKeyWord getInductor();
    protected abstract ArgumentType<T> getArgumentType();

    protected abstract T defaultValue();

    public T parse(StringReader reader) throws CommandSyntaxException
    {

        if (!ArgumentUtil.validateSpellKeyWord(reader, getInductor()))  // Not a valid inducer
        {
            return defaultValue();
        }
        else                                                            // Correct inducer, parsing start
        {
            return getArgumentType().parse(reader);
        }
    }
}
