package com.ustctuixue.arcaneart.api.spell.translator;

import lombok.Data;

@Data
public class RawSpell
{
    final String name;

    final String incantations;

    public RawSpell(String name, String incantations)
    {
        this.name = name;
        this.incantations = incantations;
    }
}
