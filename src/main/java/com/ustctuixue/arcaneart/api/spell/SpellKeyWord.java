package com.ustctuixue.arcaneart.api.spell;

import lombok.*;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpellKeyWord implements IForgeRegistryEntry<SpellKeyWord>
{
    public static ForgeRegistry<SpellKeyWord> REGISTRY;

    @Getter
    private final ExecuteType type;

    public SpellKeyWord (Property property)
    {
        this.type = property.type;
    }



    public final String getTranslationPath()
    {
        return name.getNamespace() + "." + name.getPath();
    }

    /////////////////////////////////////////
    // Registry Start
    /////////////////////////////////////////
    private ResourceLocation name;

    @Override
    public final SpellKeyWord setRegistryName(ResourceLocation name)
    {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public final ResourceLocation getRegistryName()
    {
        return this.name;
    }


    @Override
    public final Class<SpellKeyWord> getRegistryType()
    {
        return SpellKeyWord.class;
    }

    static String encode(String word)
    {
        int id = REGISTRY.getID(new ResourceLocation(word));
        if (id != -1)
        {
            return String.format("kw:%04d", id);
        }
        return word;
    }

    static String decode(String word)
    {
        if (word.matches("kw:[0-9]{4}"))
        {
            SpellKeyWord keyWord = REGISTRY.getValue(Integer.getInteger(word.substring(3)));
            ResourceLocation rl = keyWord.getRegistryName();
            if (rl != null)
            {
                return rl.toString();
            }
        }
        return word;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SpellKeyWord)
        {
            return this.getRegistryName().equals(((SpellKeyWord) obj).getRegistryName());
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.getRegistryName().toString();
    }


    //////////////////////////////////////////////////////////////////
    // Properties
    //////////////////////////////////////////////////////////////////


    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)

    public static class Property
    {
        @Getter @With @Nonnull
        private ExecuteType type = ExecuteType.NOT_EXECUTABLE;

    }

    public enum ExecuteType
    {
        COMMON, ON_HOLD, ON_RELEASE, NOT_EXECUTABLE
    }
}
