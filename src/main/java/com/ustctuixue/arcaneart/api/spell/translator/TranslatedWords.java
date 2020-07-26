package com.ustctuixue.arcaneart.api.spell.translator;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class TranslatedWords
{

    private final List<String> words = Lists.newArrayList();

    @Override
    public String toString()
    {
        return String.join("|", words);
    }

    public Pattern getPattern()
    {
        return Pattern.compile("\\b" +
                String.join("\\b|\\b", (String[])words.toArray()) + "\\b",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
    }

    public TranslatedWords add(String word)
    {
        this.words.add(word);
        return this;
    }

    public TranslatedWords addAll(String... words)
    {
        return this.addAll(Arrays.asList(words));

    }

    public TranslatedWords addAll(Collection<String> words)
    {
        this.words.addAll(words);
        return this;
    }

    static TranslatedWords singleton(String word)
    {
        TranslatedWords words = new TranslatedWords();
        words.add(word);
        return words;
    }

    public static TranslatedWords fromVerticalBarSeparatedString(String s)
    {
        TranslatedWords words = new TranslatedWords();
        words.addAll(s.split("\\|"));
        return words;
    }

    public static TranslatedWords fromStrings(String... wordArray)
    {
        TranslatedWords words = new TranslatedWords();
        words.addAll(wordArray);
        return words;
    }

    public static TranslatedWords fromList(List<String> list)
    {
        TranslatedWords words = new TranslatedWords();
        words.addAll(list);
        return words;
    }

    public List<String> toList()
    {
        return Lists.newArrayList(this.words);
    }
}
