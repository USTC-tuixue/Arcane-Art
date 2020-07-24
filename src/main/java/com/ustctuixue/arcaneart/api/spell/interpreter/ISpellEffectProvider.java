package com.ustctuixue.arcaneart.api.spell.interpreter;

public interface ISpellEffectProvider
{
    SpellSideEffect build(SpellCasterSource source);
}
