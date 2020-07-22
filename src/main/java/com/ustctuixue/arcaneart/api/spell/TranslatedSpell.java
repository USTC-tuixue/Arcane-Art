package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

public class TranslatedSpell
{
    @Getter @Setter
    private String name;

    @Getter
    private final List<String> commonSentences = Lists.newArrayList();

    @Getter
    private final List<String> onHoldSentences = Lists.newArrayList();

    @Getter
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

}
