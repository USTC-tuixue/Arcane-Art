package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SpellKeyWord implements IForgeRegistryEntry<SpellKeyWord>
{

    private Map<String, String> defaultTranslations = Maps.newHashMap();
    public static IForgeRegistry<SpellKeyWord> REGISTRY;

    /**
     * Add a translation to this keyword
     * @param language language id
     * @param translation translation for the keyword in this language in all forms
     */
    public void addTranslation(String language, String... translation)
    {
        defaultTranslations.put(language, Strings.join(translation, "|"));
    }

    public final Set<Map.Entry<String, String>> getDefaultTranslationEntrySet()
    {
        return defaultTranslations.entrySet();
    }

    public final Set<String> getDefaultTranslationKeySet()
    {
        return defaultTranslations.keySet();
    }

    /**
     * Get default translation for the keyword in @var language
     * @param language specified language
     * @return translation regex pattern
     */
    @Nullable
    public final String getDefaultTranslation(String language)
    {
        return defaultTranslations.getOrDefault(language, getRegistryName().toString());
    }

    public final Collection<String> getDefaultTranslationValueSet()
    {
        return defaultTranslations.values();
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
}
