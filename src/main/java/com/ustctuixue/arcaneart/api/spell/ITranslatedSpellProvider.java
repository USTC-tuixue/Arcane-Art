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

    void compile(SpellCasterSource source);

    List<ParseResults<SpellCasterSource>> getCompileResults(SpellCasterSource source);

    class Impl implements ITranslatedSpellProvider
    {
        @Getter
        private TranslatedSpell spell;

        private boolean parsed = false;
        private List<ParseResults<SpellCasterSource>> parseResults;

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
        public void compile(SpellCasterSource source)
        {
            SpellDispatcher.executeSpell(spell.getCommonSentences(), source);
            SpellDispatcher.parseSpell(spell.getOnHoldSentences(), source);
            parsed = true;
        }

        @Override
        public List<ParseResults<SpellCasterSource>> getCompileResults(SpellCasterSource source)
        {
            if (!parsed)
            {
                compile(source);
            }
            return parseResults;
        }
    }
}
