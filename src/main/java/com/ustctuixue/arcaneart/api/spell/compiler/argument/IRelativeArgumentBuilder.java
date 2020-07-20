package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;

/**
 * Arguments for spells which are relative to caster
 * @param <T> return type
 */
public interface IRelativeArgumentBuilder<T>
{
    T build(SpellBuilder source);
}
