package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;

import javax.annotation.Nonnull;

/**
 * Arguments for spells which are relative to caster
 * @param <T> return type
 */
public interface IRelativeArgumentBuilder<T>
{
    T build(@Nonnull SpellCasterSource source);
}
