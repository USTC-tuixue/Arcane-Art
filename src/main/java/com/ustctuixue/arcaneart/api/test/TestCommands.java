package com.ustctuixue.arcaneart.api.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.spell.translator.SpellTranslator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class TestCommands
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        new TestTranslationCommand(dispatcher);
    }


    public static class TestTranslationCommand
    {
        public TestTranslationCommand(CommandDispatcher<CommandSource> dispatcher)
        {
            dispatcher.register(
                    Commands.literal("test_translate")
                            .then(Commands.argument("spell", StringArgumentType.string())
                                    .executes((s)-> translate(s.getSource(), s.getArgument("spell", String.class))))
            );
        }

        int translate(CommandSource source, String s)
        {
            List<String> stringList = Arrays.asList(s.split("."));
            LanguageProfile profile = LanguageManager.getInstance().getBestMatchedProfile(stringList);
            stringList = SpellTranslator.translateByProfile(stringList, profile);
            for (String t :
                    stringList)
            {
                ITextComponent component = new TranslationTextComponent(t);
                source.sendFeedback(component, true);
            }
            return 1;
        }
    }

}
