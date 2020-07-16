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

    public Set<Map.Entry<String, String>> getDefaultTranslationEntrySet()
    {
        return defaultTranslations.entrySet();
    }

    public Set<String> getDefaultTranslationKeySet()
    {
        return defaultTranslations.keySet();
    }

    /**
     * Get default translation for the keyword in @var language
     * @param language specified language
     * @return translation regex pattern
     */
    @Nullable
    public String getDefaultTranslation(String language)
    {
        return defaultTranslations.getOrDefault(language, null);
    }

    public Collection<String> getDefaultTranslationValueSet()
    {
        return defaultTranslations.values();
    }

    public String getTranslationPath()
    {
        return name.getNamespace() + "." + name.getPath();
    }

    /////////////////////////////////////////
    // Registry Start
    /////////////////////////////////////////
    private ResourceLocation name;

    @Override
    public SpellKeyWord setRegistryName(ResourceLocation name)
    {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName()
    {
        return this.name;
    }


    @Override
    public Class<SpellKeyWord> getRegistryType()
    {
        return SpellKeyWord.class;
    }
}
