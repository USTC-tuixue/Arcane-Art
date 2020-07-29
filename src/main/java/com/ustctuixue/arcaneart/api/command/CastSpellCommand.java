package com.ustctuixue.arcaneart.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.Variable;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;

public class CastSpellCommand extends AbstractCommandImpl
{

    public CastSpellCommand(CommandDispatcher<CommandSource> dispatcher)
    {
        super(dispatcher);

        dispatcher.register(Commands.literal("castspell").requires(source -> source.hasPermissionLevel(2))
            .then(Commands.argument("spell", StringArgumentType.string())
                    .executes(context -> {
                        SpellCasterSource source = SpellCasterSource.fromCommandSource(context.getSource());
                        RawSpell rawSpell = RawSpell.namelessSpell(context.getArgument("spell", String.class));
                        TranslatedSpell translatedSpell = TranslatedSpell.fromRawSpell(rawSpell);
                        if (translatedSpell != null)
                        {
                            Interpreter.executeSpell(translatedSpell, source);
                            context.getSource().sendFeedback(
                                    new StringTextComponent("Variables:"), true
                            );
                            for (Map.Entry<String, Object> entry :
                                    source.getVariables().entrySet())
                            {
                                context.getSource().sendFeedback(
                                        new StringTextComponent(entry.getKey() + " : " + entry.toString()), true
                                );
                            }
                            return 1;
                        }
                        else
                            return 0;
                    })
            )
        );
    }
}
