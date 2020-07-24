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
    private T value = null;
    public Variable(String name, Class<T> clazz)
    {
        this.name = name;
        this.type = clazz;
    }

    @SuppressWarnings("unchecked")
    public Variable(T value)
    {
        this.type = (Class<T>) value.getClass();
        this.value = value;
    }

    public Variable(Class<T> cls, T value)
    {
        this.type = cls;
        this.value = value;
    }

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
