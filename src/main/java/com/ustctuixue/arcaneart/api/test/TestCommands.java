package com.ustctuixue.arcaneart.api.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.translator.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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

            LanguageProfile profile = LanguageManager.getInstance().getBestMatchedProfile(s);
            if (profile != null)
            {
                source.sendFeedback(new TranslationTextComponent("Language: " + profile.getName()), true);
                TranslatedSpell spell = TranslatedSpell.translateFromRawSpell(new RawSpell("", s), profile);
                assert spell != null;
                try
                {
                    source.sendFeedback(new TranslationTextComponent("Common:"), true);
                    for (String t : spell.getCommonSentences())
                    {
                        ITextComponent component = new TranslationTextComponent(t);
                        source.sendFeedback(component, true);
                    }
                    source.sendFeedback(new TranslationTextComponent("On Hold:"), true);
                    for (String t : spell.getOnHoldSentences())
                    {
                        ITextComponent component = new TranslationTextComponent(t);
                        source.sendFeedback(component, true);
                    }
                    source.sendFeedback(new TranslationTextComponent("On Release"), true);
                    for (String t : spell.getOnReleaseSentences())
                    {
                        ITextComponent component = new TranslationTextComponent(t);
                        source.sendFeedback(component, true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return 1;
            }
            else
            {
                source.sendFeedback(new TranslationTextComponent("No language matched!"), true);
            }
            return 0;
        }
    }

}
