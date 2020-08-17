package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;
import java.util.Optional;

@AllArgsConstructor
public class OptionalArgument<T> implements ArgumentType<Optional<T>>
{

    final ArgumentType<T> type;

    @Override @Nonnull
    public Optional<T> parse(StringReader reader)
    {
        try{
            return Optional.of(type.parse(reader));
        }
        catch (CommandSyntaxException e)
        {
            return Optional.empty();
        }
    }
}
