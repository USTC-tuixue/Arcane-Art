package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArgumentUtil
{
    /**
     * Get spell keyword from string reader
     * Reader will be pushed after a successful reading
     *
     * @param reader string reader
     * @return registered spell keyword
     *          If the next word is not a spell keyword, return null, and reader will be reset
     * @throws CommandSyntaxException when readStringUntil(' ') throws
     */
    @Nullable
    public static SpellKeyWord getSpellKeyWord(StringReader reader) throws CommandSyntaxException
    {
        int cursor = reader.getCursor();
        ResourceLocation inRangeKeyword = new ResourceLocation(reader.readStringUntil(' '));
        if (SpellKeyWord.REGISTRY.containsKey(inRangeKeyword))
        {
            return SpellKeyWord.REGISTRY.getValue(inRangeKeyword);
        }
        return null;
    }

    public static boolean validateSpellKeyWord(StringReader reader, Set<SpellKeyWord> validKeyWords) throws CommandSyntaxException
    {
        int cursor = reader.getCursor();
        if (!validKeyWords.contains(getSpellKeyWord(reader)))
        {
            reader.setCursor(cursor);
            return false;
        }
        return true;
    }

    /**
     * Check if the next word is specified keyword
     * If not, reader will be reset
     * @param reader reader
     * @param validKeyWord specified keyword
     * @return if the next word is specified keyword
     * @throws CommandSyntaxException same as getSpellKeyWord
     */
    public static boolean validateSpellKeyWord(StringReader reader, SpellKeyWord validKeyWord) throws CommandSyntaxException
    {
        int cursor = reader.getCursor();
        if (!validKeyWord.equals(getSpellKeyWord(reader)))
        {
            reader.setCursor(cursor);
            return false;
        }
        return true;
    }
}
