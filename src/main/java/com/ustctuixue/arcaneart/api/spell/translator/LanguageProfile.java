package com.ustctuixue.arcaneart.api.spell.translator;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import joptsimple.internal.Strings;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageProfile
{
    @Getter
    private final String name;
    private final Map<SpellKeyWord, String> keyWordMap = Maps.newHashMap();

    LanguageProfile(String name, Set<ResourceLocation> keyWords)
    {
        this.name = name;
        keyWordMap.putAll(
                Maps.asMap(keyWords.stream().map(
                        (kw)->
                                SpellKeyWord.REGISTRY.getValue(kw)).collect(Collectors.toSet()),
                        (k) ->
                            {
                                // I don't know how you registered a null into a Forge registry.
                                Objects.requireNonNull(k);
                                return k.getDefaultTranslation(name);
                            }
                )
        );
    }

    public void load(File file)
    {
        CommentedFileConfig config = CommentedFileConfig.of(file);
        config.load();
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Dumping translations:");
        keyWordMap.replaceAll((k, v) -> (String) config.getOptional(k.getTranslationPath()).orElseGet(()->keyWordMap.get(k)));
        for (Map.Entry<SpellKeyWord, String> entry:
                this.keyWordMap.entrySet())
        {
            ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, entry.getKey().getTranslationPath() + " = " + entry.getValue());
            config.add(entry.getKey().getTranslationPath(), entry.getValue());
        }
        config.save();
        config.close();
    }


    public String getPattern(ResourceLocation k)
    {
        return getPattern(SpellKeyWord.REGISTRY.getValue(k));
    }

    public String getPattern(SpellKeyWord keyWord)
    {
        return keyWordMap.getOrDefault(keyWord, "");
    }

    public String getAllPatterns()
    {
        return Strings.join(keyWordMap.values(), "|");
    }

    public String translate(String raw)
    {
        String m = raw;
        for (Map.Entry<SpellKeyWord, String> entry :
                this.keyWordMap.entrySet())
        {
            ResourceLocation rl = entry.getKey().getRegistryName();
            Objects.requireNonNull(rl);
            m = m.replaceAll(entry.getValue(), rl.toString());
        }
        return m;
    }
}
