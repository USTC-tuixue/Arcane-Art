package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellContainer;
import lombok.Getter;

public interface ITranslatedSpellProvider
{
    TranslatedSpell getSpell();
    ITranslatedSpellProvider setSpell(TranslatedSpell spellIn);

    boolean hasSpell();

    SpellContainer getCompiled(SpellCasterSource source);

    @Deprecated
    ITranslatedSpellProvider setCompiled(SpellContainer compiled);

    void compile(SpellCasterSource source);

    class Impl implements ITranslatedSpellProvider
    {
        @Getter
        private TranslatedSpell spell;

        private boolean dirty = true;
        private SpellContainer compiled;

        private SpellCasterSource previousSource = null;

        public Impl()
        {
            this.spell = new TranslatedSpell();
        }

        public Impl(TranslatedSpell spellIn)
        {
            this.spell = spellIn;
        }

        @Override
        public boolean hasSpell()
        {
            return !spell.isEmpty();
        }

        public ITranslatedSpellProvider setSpell(TranslatedSpell spellIn)
        {
            this.spell = spellIn;
            dirty = true;
            return this;
        }

        @Override
        public SpellContainer getCompiled(SpellCasterSource source)
        {
            if (this.dirty || this.previousSource != source)
            {
                compile(source);
            }
            return this.compiled;
        }

        @Override
        public void compile(SpellCasterSource source)
        {
            this.dirty = false;
            this.compiled = Interpreter.compile(this.spell);
            this.previousSource = source;
        }

        @Override
        public ITranslatedSpellProvider setCompiled(SpellContainer compiled)
        {
            this.compiled = compiled;
            return this;
        }
    }
}
