package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;

/**
 * Arguments for spells which are relative to caster
 * @param <T> return type
 */
public interface IRelativeArgumentBuilder<T>
{
    T build(SpellCasterSource source);
}
