package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import lombok.Data;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@Data
public class TranslatedSpell
{
    private String name;

    private final List<String> preProcessSentences = Lists.newArrayList();

    private final List<String> onHoldSentences = Lists.newArrayList();

    private final List<String> onReleaseSentences = Lists.newArrayList();

    @Nullable
    private final SpellAuthor author;

    @SuppressWarnings("WeakerAccess")
    public TranslatedSpell()
    {
        this("", null);
    }

    public TranslatedSpell(String name)
    {
        this(name, null);
    }

    @SuppressWarnings("WeakerAccess")
    public TranslatedSpell(String name, @Nullable SpellAuthor authorIn)
    {
        this.name = name;
        this.author = authorIn;
    }

    @SuppressWarnings(value={"WeakerAccess", "UnusedReturnValue"})
    public TranslatedSpell addCommonSentence(String s)
    {
        preProcessSentences.add(s);
        return this;
    }

    @SuppressWarnings(value={"WeakerAccess", "UnusedReturnValue"})
    public TranslatedSpell addOnHoldSentence(String s)
    {
        onHoldSentences.add(s);
        return this;
    }

    @SuppressWarnings(value={"WeakerAccess", "UnusedReturnValue"})
    public TranslatedSpell addOnReleaseSentence(String s)
    {
        onReleaseSentences.add(s);
        return this;
    }

    @SuppressWarnings(value={"WeakerAccess", "UnusedReturnValue"})
    public TranslatedSpell addAllCommonSentences(Collection<String> sentences)
    {
        preProcessSentences.addAll(sentences);
        return this;
    }

    @SuppressWarnings(value={"WeakerAccess", "UnusedReturnValue"})
    public TranslatedSpell addAllOnHoldSentences(Collection<String> sentences)
    {
        onHoldSentences.addAll(sentences);
        return this;
    }

    @SuppressWarnings(value={"UnusedReturnValue", "unused"})
    public TranslatedSpell addAllOnReleaseSentences(Collection<String> sentences)
    {
        onReleaseSentences.addAll(sentences);
        return this;
    }

    public boolean isEmpty()
    {
        return this.onHoldSentences.isEmpty() && this.onReleaseSentences.isEmpty() && this.preProcessSentences.isEmpty();
    }

    @Nullable
    public static TranslatedSpell fromWrittenBook(ItemStack writtenBookStack)
    {
        if(writtenBookStack.getItem() instanceof WrittenBookItem)
        {
            RawSpell rawSpell = RawSpell.fromWrittenBook(writtenBookStack);
            if (rawSpell != null)
            {
                LanguageProfile profile = LanguageManager.getInstance().getBestMatchedProfile(rawSpell.getIncantations());
                if (profile != null)
                {
                    return translateFromRawSpell(rawSpell, profile);
                }
            }
        }
        return null;
    }

    public static SpellKeyWord getFirstKeyWord(final String translatedSentence)
    {
        StringReader reader = new StringReader(translatedSentence);
        reader.skipWhitespace();
        try
        {
            return getFirstKeyWord(reader);
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public static SpellKeyWord getFirstKeyWord(StringReader reader) throws CommandSyntaxException
    {
        reader.skipWhitespace();
        ResourceLocation resourceLocation = ResourceLocationArgument.resourceLocation().parse(reader);
        return SpellKeyWord.REGISTRY.getValue(resourceLocation);
    }

    @Nullable
    public static TranslatedSpell fromRawSpell(RawSpell rawSpell)
    {
        LanguageProfile profile = LanguageManager.getInstance().getBestMatchedProfile(rawSpell.getIncantations());
        if (profile != null)
        {
            return translateFromRawSpell(rawSpell, profile);
        }
        return null;
    }

    @Nullable
    public static TranslatedSpell translateFromRawSpell(RawSpell rawSpell, LanguageProfile profile)
    {
        List<String> translated = profile.translate(rawSpell.getIncantations());
        TranslatedSpell result = new TranslatedSpell(rawSpell.getName(), rawSpell.getAuthor());
        for (String s : translated)
        {
            SpellKeyWord kw = getFirstKeyWord(s);
            if (kw == null)
            {
                ArcaneArtAPI.LOGGER.info(LanguageManager.LANGUAGE, "First word is not registered!");
                ArcaneArtAPI.LOGGER.info(LanguageManager.LANGUAGE, "Line: " + s);
                return null;
            }
            switch (kw.getType())
            {
                case ON_HOLD:
                    result.addOnHoldSentence(s);
                    break;
                case ON_RELEASE:
                    result.addOnReleaseSentence(s);
                    break;
                default:
                    result.addCommonSentence(s);
            }
        }

        return result;
    }
}
