package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DefaultedArgument<V> implements ArgumentType<V>
{
    @Getter
    private final ArgumentType<V> argumentParser;
    @Getter
    private final V defaultedValue;

    @Override
    public V parse(StringReader reader)
    {
        try{
            return argumentParser.parse(reader);
        } catch (CommandSyntaxException e)
        {
            return defaultedValue;
        }
    }
}
