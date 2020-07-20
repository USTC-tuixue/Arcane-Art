package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.spell.compiler.argument.Variable;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class VariableArgument<T> implements ArgumentType<Variable<T>>
{

    private static final SimpleCommandExceptionType INCOMPLETE_QUOTES =
            new SimpleCommandExceptionType(new TranslationTextComponent("spell.exception.incomplete_quotes"));
    private static final SimpleCommandExceptionType INCONSISTENT_QUOTES =
            new SimpleCommandExceptionType(new TranslationTextComponent("spell.exception.inconsistent_quotes"));

    protected abstract Class<T> getType();

    protected abstract ArgumentType<T> getArgumentType();

    @Override
    public Variable<T> parse(StringReader reader) throws CommandSyntaxException
    {
        if (reader.canRead())
        {
            int quoteId = APIConfig.Spell.LEFT_QUOTES.get().indexOf(reader.peek());
            int rightQuoteId;
            int cursor = reader.getCursor();
            StringBuilder builder = new StringBuilder();
            if (quoteId != -1)
            {
                do
                {
                    builder.append(reader.read());
                    rightQuoteId = APIConfig.Spell.RIGHT_QUOTES.get().indexOf(reader.peek());
                }while (reader.canRead() && rightQuoteId != -1);

                if (rightQuoteId == -1)         // Quotes must come in pairs
                {
                    reader.setCursor(cursor);
                    throw INCOMPLETE_QUOTES.createWithContext(reader);
                }

                if (rightQuoteId != quoteId)    // Quotes must match
                {
                    reader.setCursor(cursor);
                    throw INCONSISTENT_QUOTES.createWithContext(reader);
                }
                else                            // Found a variable
                {
                    return new Variable<>(builder.toString(), this.getType());
                }
            }
            else                                // If not a variable form
            {
                return new Variable<>(this.getType(), this.getArgumentType().parse(reader));
            }
        }
        reader.setCursor(0);
        throw INCONSISTENT_QUOTES.createWithContext(reader);
    }
}
