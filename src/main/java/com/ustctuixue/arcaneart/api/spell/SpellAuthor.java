package com.ustctuixue.arcaneart.api.spell;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data @AllArgsConstructor
public class SpellAuthor
{
    private final String name;


    @Override
    public String toString()
    {
        return this.name;
    }
}
