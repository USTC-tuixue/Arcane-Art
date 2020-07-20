package com.ustctuixue.arcaneart.api.spell.translator;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;
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
    private String leftQuote = "";
    private String rightQuote = "";
    private String period = "";

    LanguageProfile(String name)
    {
        this.name = name;
        Set<ResourceLocation> keyWords = SpellKeyWord.REGISTRY.getKeys();
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

    void load(File file)
    {
        CommentedFileConfig config = CommentedFileConfig.of(file);
        config.load();
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Dumping translations:");

        keyWordMap.replaceAll((k, v) -> (String) config.getOptional(k.getTranslationPath()).orElseGet(()->keyWordMap.get(k)));
        this.leftQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.leftQuote").orElse(this.leftQuote);
        this.rightQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.rightQuote").orElse(this.rightQuote);
        this.period = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.period").orElse(this.period);

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

    String getPattern(SpellKeyWord keyWord)
    {
        return keyWordMap.getOrDefault(keyWord, "");
    }

    String getAllPatterns()
    {
        return Strings.join(keyWordMap.values(), "|");
    }

    /**
     *
     * @param sentence 语句
     * @return 翻译后的语句
     */
    public String translate(String sentence)
    {
        String m = sentence;
        for (Map.Entry<SpellKeyWord, String> entry :
                this.keyWordMap.entrySet())
        {
            ResourceLocation rl = entry.getKey().getRegistryName();
            Objects.requireNonNull(rl);
            m = sentence.replaceAll(entry.getValue(), rl.toString());
        }
        return m;
    }
}
