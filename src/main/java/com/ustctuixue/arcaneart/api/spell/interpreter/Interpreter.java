package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.APIRegistries;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Interpreter
{
    private Interpreter(){}

    public static Map<SpellKeyWord, Supplier<ISpell>> SPELLS = Maps.newHashMap();

    private static int compilePerProcess(List<String> processSpellList, List<ISpell> processCompiledSpells)
    {
        int succeeded = 0;
        for (String s : processSpellList)
        {
            StringReader reader = new StringReader(s);

            // Get effective keyword
            SpellKeyWord kw;
            try
            {
                kw = TranslatedSpell.getFirstKeyWord(reader);
                ArcaneArtAPI.LOGGER.debug("Spell keyword: " + kw);
            } catch (CommandSyntaxException e)
            {
                reader.skipWhitespace();
                ArcaneArtAPI.LOGGER.debug("Ineffective keyword: " + reader.getRemaining());
                e.printStackTrace();
                return succeeded;
            }

            // Get spell supplier
            Supplier<ISpell> spellSupplier = SPELLS.getOrDefault(kw, null);
            if (spellSupplier != null)
            {
                ISpell compiledSpell = spellSupplier.get();
                boolean flag = compiledSpell.parse(reader);
                if (flag)
                {
                    ArcaneArtAPI.LOGGER.info("Successfully parsed spell");
                    processCompiledSpells.add(compiledSpell);
                    succeeded++;
                }
                else
                {
                    ArcaneArtAPI.LOGGER.info("Parse failed for spell: " + kw);
                    ArcaneArtAPI.LOGGER.info("Spell: " + reader.getString());
                    return succeeded;
                }
            }
            else
            {
                ArcaneArtAPI.LOGGER.info("Keyword " + kw + " cannot provide a spell parser!");
                return succeeded;
            }
        }
        return succeeded;
    }

    @Nonnull
    public static SpellContainer compile(@Nonnull TranslatedSpell spell)
    {
        SpellContainer container = new SpellContainer();
        compilePerProcess(spell.getPreProcessSentences(), container.preProcess);
        compilePerProcess(spell.getOnHoldSentences(), container.onHold);
        compilePerProcess(spell.getOnReleaseSentences(), container.onRelease);
        return container;
    }

    public static ItemStack getItemSpell(TranslatedSpell spell, @Nonnull ItemSpell itemSpell)
    {
        if (spell == null)
        {
            return ItemStack.EMPTY;
        }

        SpellContainer container = compile(spell);
        if (!container.isEmpty())
        {
            ItemStack stack = new ItemStack(itemSpell);
            itemSpell.setSpell(stack, spell, container);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static int executeSpell(TranslatedSpell spell, SpellCasterSource source)
    {
        SpellContainer container = compile(spell);
        ArcaneArtAPI.LOGGER.debug("Executing pre processors");
        container.executePreProcess(source);
        ArcaneArtAPI.LOGGER.debug("Executing on holds");
        container.executeOnHold(source);
        ArcaneArtAPI.LOGGER.debug("Executing on release");
        container.executeOnRelease(source);
        return 1;
    }

    public static ItemStack fromWrittenBook(ItemStack book)
    {
        TranslatedSpell spell = TranslatedSpell.fromWrittenBook(book);
        return getItemSpell(spell, APIRegistries.Items.ITEM_SPELL);
    }
}
