package com.ustctuixue.arcaneart.api.spell;

import lombok.Getter;

public interface ITranslatedSpellProvider
{
    TranslatedSpell getSpell();
    ITranslatedSpellProvider setSpell(TranslatedSpell spellIn);

    class Impl implements ITranslatedSpellProvider
    {
        @Getter
        TranslatedSpell spell;

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
            return this;
        }
    }
}
