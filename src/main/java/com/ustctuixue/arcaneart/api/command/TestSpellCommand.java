package com.ustctuixue.arcaneart.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TestSpellCommand extends AbstractCommandImpl
{

    public TestSpellCommand(CommandDispatcher<CommandSource> dispatcher)
    {
        super(dispatcher);

        dispatcher.register(Commands.literal("castspell").requires(source -> source.hasPermissionLevel(2))
            .then(Commands.argument("spell", StringArgumentType.string())
                    .executes(context -> {
                        SpellCasterSource source = SpellCasterSource.fromCommandSource(context.getSource());
                        RawSpell rawSpell = RawSpell.namelessSpell(context.getArgument("spell", String.class));
                        TranslatedSpell translatedSpell = TranslatedSpell.fromRawSpell(rawSpell);
                        if (translatedSpell != null)
                            return SpellDispatcher.executeSpell(translatedSpell.getCommonSentences(), source);
                        else
                            return 0;
                    })
            )
        );
    }
}
