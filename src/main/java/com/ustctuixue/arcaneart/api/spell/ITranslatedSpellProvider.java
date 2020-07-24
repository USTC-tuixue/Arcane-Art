package com.ustctuixue.arcaneart.api.spell;

import com.mojang.brigadier.ParseResults;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import lombok.Getter;

import java.util.List;

public interface ITranslatedSpellProvider
{
    TranslatedSpell getSpell();
    ITranslatedSpellProvider setSpell(TranslatedSpell spellIn);

    /**
     * Execute common sentences
     * @param source compile source
     */
    void preCompile(SpellCasterSource source);

    /**
     * Force re-parsing persistent sentences and store parsed results
     *
     * @param source compile source
     */
    void parseOnHold(SpellCasterSource source);

    /**
     * Retrieve parsed persistent sentences results
     * If this provider is not parsed or source is not equal to previously used in parsing,
     * this function will re-parse first, then return parse results.
     *
     * Because parsed results is relative to SpellCasterSource,
     * you should re-compile if source does not match.
     *
     * @param source compile source
     * @return parsed results
     */
    List<ParseResults<SpellCasterSource>> getCompileResults(SpellCasterSource source);

    class Impl implements ITranslatedSpellProvider
    {
        @Getter
        private TranslatedSpell spell;

        private boolean parsed = false;
        private List<ParseResults<SpellCasterSource>> parseResults;

        private SpellCasterSource previousSource = null;

        public Impl()
        {
            this.spell = new TranslatedSpell();
        }

        public Impl(TranslatedSpell spellIn)
        {
            this.spell = spellIn;
        }


        public ITranslatedSpellProvider setSpell(TranslatedSpell spellIn)
        {
            this.spell = spellIn;
            parsed = false;
            return this;
        }

        @Override
        public void preCompile(SpellCasterSource source)
        {
            SpellDispatcher.executeSpell(spell.getCommonSentences(), source);
        }

        @Override
        public void parseOnHold(SpellCasterSource source)
        {
            parseResults = SpellDispatcher.parseSpell(spell.getOnHoldSentences(), source);
            previousSource = source;
            parsed = true;
        }

        @Override
        public List<ParseResults<SpellCasterSource>> getCompileResults(SpellCasterSource source)
        {
            if (!parsed || previousSource != source)
            {
                preCompile(source);
                parseOnHold(source);
            }
            return parseResults;
        }
    }
}
