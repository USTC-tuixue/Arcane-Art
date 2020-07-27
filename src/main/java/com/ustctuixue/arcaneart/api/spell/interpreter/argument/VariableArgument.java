package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class VariableArgument<T> implements ArgumentType<Variable<T>>
{
    private static final SimpleCommandExceptionType INCONSISTENT_QUOTES =
            new SimpleCommandExceptionType(new TranslationTextComponent("spell.exception.inconsistent_quotes"));

    protected abstract Class<T> getType();

    protected abstract ArgumentType<T> getArgumentType();

    @Override
    public Variable<T> parse(StringReader reader) throws CommandSyntaxException
    {
        if (reader.canRead())
        {
            reader.skipWhitespace();
            char next = reader.peek();
            if (StringReader.isQuotedStringStart(next))                  // If is a variable
            {
                ArcaneArtAPI.LOGGER.info("variable");
                return new Variable<>(reader.readStringUntil(next), this.getType());
            }
            else                                // If not a variable form
            {
                ArcaneArtAPI.LOGGER.info("Not a variable");
                return new Variable<>(this.getType(), this.getArgumentType().parse(reader));
            }
        }
        throw INCONSISTENT_QUOTES.createWithContext(reader);
    }
}
