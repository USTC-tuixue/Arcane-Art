package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class Interpreter
{
    private Interpreter(){}

    public static Map<SpellKeyWord, Supplier<ISpell>> SPELLS = Maps.newHashMap();

    @Nullable
    public static SpellContainer compile(TranslatedSpell spell, SpellCasterSource caster)
    {
        SpellContainer container = new SpellContainer();
        for (String s :
                spell.getCommonSentences())
        {
            StringReader reader = new StringReader(s);
            SpellKeyWord kw;
            try
            {
                kw = TranslatedSpell.getFirstKeyWord(reader);
            } catch (CommandSyntaxException e)
            {
                e.printStackTrace();
                return null;
            }
            Supplier<ISpell> spellSupplier = SPELLS.getOrDefault(kw, null);
            if (spellSupplier != null)
            {
                ISpell compiledSpell = spellSupplier.get();
                boolean flag = compiledSpell.parse(reader);
                if (flag)
                {
                    container.preProcess.add(compiledSpell);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        for (String s :
                spell.getOnReleaseSentences())
        {
            StringReader reader = new StringReader(s);
            SpellKeyWord kw;
            try
            {
                kw = TranslatedSpell.getFirstKeyWord(reader);
            } catch (CommandSyntaxException e)
            {
                e.printStackTrace();
                return null;
            }
            Supplier<ISpell> spellSupplier = SPELLS.get(kw);
            if (spellSupplier != null)
            {
                ISpell compiledSpell = spellSupplier.get();
                boolean flag = compiledSpell.parse(reader);
                if (flag)
                {
                    container.onRelease.add(compiledSpell);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        for (String s :
                spell.getOnHoldSentences())
        {
            StringReader reader = new StringReader(s);
            SpellKeyWord kw;
            try
            {
                kw = TranslatedSpell.getFirstKeyWord(reader);
            } catch (CommandSyntaxException e)
            {
                e.printStackTrace();
                return null;
            }
            Supplier<ISpell> spellSupplier = SPELLS.get(kw);
            if (spellSupplier != null)
            {
                ISpell compiledSpell = spellSupplier.get();
                boolean flag = compiledSpell.parse(reader);
                if (flag)
                {
                    container.onHold.add(compiledSpell);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        return container;
    }

    public static ItemStack getItemSpell(TranslatedSpell spell, SpellCasterSource caster, @Nonnull ItemSpell itemSpell)
    {
        SpellContainer container = compile(spell, caster);
        if (container != null && !container.isEmpty())
        {
            ItemStack stack = new ItemStack(itemSpell);
            itemSpell.setSpell(stack, spell, container);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static int executeSpell(TranslatedSpell spell, SpellCasterSource source)
    {
        SpellContainer container = compile(spell, source);
        if (container != null)
        {
            container.executePreProcess(source);
            container.executeOnHold(source);
            container.executeOnRelease(source);
            return 1;
        }
        return 0;
    }

}
