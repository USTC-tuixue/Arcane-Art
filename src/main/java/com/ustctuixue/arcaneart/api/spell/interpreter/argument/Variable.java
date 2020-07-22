package com.ustctuixue.arcaneart.api.spell.interpreter.argument;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Variable<T>
{
    @Getter @Setter
    String name;

    @Getter @NonNull
    Class<T> type;

    public Variable(String name, Class<T> clazz)
    {
        this.name = name;
        this.type = clazz;
    }

    public Variable(Class<T> clazz, T value)
    {
        this.type = clazz;
        this.value = value;
    }

    private T value = null;

    public T getValueFromSpellBuilder(SpellCasterSource source)
    {
        return source.getVariable(this.name, type);
    }

    public T get()
    {
        return value;
    }

    public Variable<T> set(T value)
    {
        this.value = value;
        return this;
    }
}
