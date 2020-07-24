package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class TranslatedSpell
{
    @Getter @Setter
    private String name;

    @Getter @Nonnull
    private final List<String> commonSentences = Lists.newArrayList();

    @Getter @Nonnull
    private final List<String> onHoldSentences = Lists.newArrayList();

    @Getter @Nonnull
    private final List<String> onReleaseSentences = Lists.newArrayList();

    public TranslatedSpell()
    {
        this("");
    }

    public TranslatedSpell(String name)
    {
        this.name = name;
    }


    public TranslatedSpell addCommonSentence(String s)
    {
        commonSentences.add(s);
        return this;
    }

    public TranslatedSpell addOnHoldSentence(String s)
    {
        onHoldSentences.add(s);
        return this;
    }

    public TranslatedSpell addOnReleaseSentence(String s)
    {
        onReleaseSentences.add(s);
        return this;
    }

    public TranslatedSpell addAllCommonSentences(Collection<String> sentences)
    {
        commonSentences.addAll(sentences);
        return this;
    }

    public TranslatedSpell addAllOnHoldSentences(Collection<String> sentences)
    {
        onHoldSentences.addAll(sentences);
        return this;
    }

    public TranslatedSpell addAllOnReleaseSentences(Collection<String> sentences)
    {
        onReleaseSentences.addAll(sentences);
        return this;
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

    private static SpellKeyWord getFirstKeyWord(final String translatedSentence)
    {
        int wordEnd = translatedSentence.indexOf(' ');
        wordEnd = wordEnd == -1? translatedSentence.length():wordEnd;
        String rl = translatedSentence.substring(0, wordEnd);
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "First word: " + rl);
        return SpellKeyWord.REGISTRY.getValue(new ResourceLocation(rl));
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
        TranslatedSpell result = new TranslatedSpell(rawSpell.getName());
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
