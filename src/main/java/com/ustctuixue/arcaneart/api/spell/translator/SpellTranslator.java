package com.ustctuixue.arcaneart.api.spell.translator;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class SpellTranslator
{
    @Nullable
    public static RawSpell joinFromWrittenBook(ItemStack itemStack)
    {
        CompoundNBT compoundNBT = itemStack.getTag();
        if (compoundNBT != null)
        {
            ListNBT pages = itemStack.getTag().getList("pages", 8);
            StringBuilder buffer = new StringBuilder();
            for (INBT page : pages)
            {
                String pageContent = page.getString();
                if (!pageContent.endsWith("-")) // 连字符

                {
                    buffer.append(" ");
                }
                buffer.append(pageContent);
            }

            return new RawSpell(
                    Items.WRITTEN_BOOK.getDisplayName(itemStack).getFormattedText(),
                    buffer.toString().replaceAll("-", "")
            );


        }
        return null;
    }

    static SpellKeyWord getFirstKeyWord(final String translatedSentence)
    {
        int wordEnd = translatedSentence.indexOf(' ');
        wordEnd = wordEnd == -1? translatedSentence.length():wordEnd;
        String rl = translatedSentence.substring(0, wordEnd);
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "First word: " + rl);
        return SpellKeyWord.REGISTRY.getValue(new ResourceLocation(rl));
    }

    @Nullable
    public static TranslatedSpell translateByProfile(RawSpell rawSpell, LanguageProfile profile)
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
