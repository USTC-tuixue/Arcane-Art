package com.ustctuixue.arcaneart.api.spell.translator;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import joptsimple.internal.Strings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LanguageProfile
{
    @Getter
    private final String name;
    private final Map<SpellKeyWord, TranslatedWords> keyWordMap = Maps.newHashMap();

    // To refer to variables
    @Setter @Getter
    private String leftQuote = "\"";
    @Setter @Getter
    private String rightQuote = "\"";

    // To divide incantations
    @Setter @Getter
    private String semicolon = ";";

    LanguageProfile(String name)
    {
        this.name = name;
    }

    private void dump()
    {
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "language: " + this.name);
        keyWordMap.forEach(((keyWord, translatedWords) ->
                ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE,
                        keyWord.toString() + " = " + translatedWords.toString())
                ));
    }

    private void loadFromRegistry()
    {
        Set<ResourceLocation> keyWords = SpellKeyWord.REGISTRY.getKeys();
        for (ResourceLocation rl :
                keyWords)
        {
            keyWordMap.putIfAbsent(SpellKeyWord.REGISTRY.getRaw(rl), TranslatedWords.singleton(rl.toString()));
        }
    }

    void load(File file)
    {
        CommentedFileConfig config = CommentedFileConfig.of(file);
        config.load();
        dump();
        loadFromRegistry();
        dump();
        ArcaneArtAPI.LOGGER.info(LanguageManager.LANGUAGE, "Loading file for language: " + this.name);

        // Load from config file
        try
        {
            keyWordMap.replaceAll((k, v) ->
                    {
                        List<String> configValue = config.get(k.getTranslationPath());
                        if (configValue != null)
                        {
                            return TranslatedWords.fromList(configValue);
                        }
                        return v;
                    }
            );
            this.leftQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.leftQuote").orElse(this.leftQuote);
            this.rightQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.rightQuote").orElse(this.rightQuote);
            this.semicolon = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.period").orElse(this.semicolon);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        dump();
        for (Map.Entry<SpellKeyWord, TranslatedWords> entry:
                this.keyWordMap.entrySet())
        {
            config.add(entry.getKey().getTranslationPath(), entry.getValue().toList());
        }
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.leftQuote", this.leftQuote);
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.rightQuote", this.rightQuote);
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.period", this.semicolon);
        config.save();
        config.close();
    }

    Pattern getAllPatterns()
    {
        return Pattern.compile(Strings.join(
                this.keyWordMap.values().stream()
                        .map(translatedWords -> translatedWords.getPattern().pattern())
                        .collect(Collectors.toSet()),
                "|"
        ));

    }

    /**
     *
     * @param sentence 语句
     * @return 翻译后的语句
     */
    public List<String> translate(String sentence)
    {
        String m = sentence;
        for (Map.Entry<SpellKeyWord, TranslatedWords> entry :
                this.keyWordMap.entrySet())
        {
            ResourceLocation rl = entry.getKey().getRegistryName();
            Objects.requireNonNull(rl);

            Pattern pattern = entry.getValue().getPattern();
            ArcaneArtAPI.LOGGER.debug(pattern.pattern());
            m = pattern.matcher(m).replaceAll(rl.toString());
        }
        m = m.replaceAll(String.valueOf(this.leftQuote), "\"");
        m = m.replaceAll(String.valueOf(this.rightQuote), "\"");
        m = m.replaceAll(String.valueOf(this.semicolon), ";");
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Translated: "+m);
        return Arrays.asList(m.split(";"));
    }

    /**
     * This will completely replace default translations
     * @param keyword spell keyword to change
     * @param translation translation word
     * @param translations translation word
     * @return this
     */
    public LanguageProfile setTranslationFor(final SpellKeyWord keyword, final String translation, String... translations)
    {
        if (translations.length != 0)
        {
            this.keyWordMap.put(keyword, TranslatedWords.fromStrings(translations).add(translation));
        }
        return this;
    }

    /**
     * Add a translation for keyword
     * @param keyword keyword to change
     * @param translation translation to be added
     * @param translations translation to be added
     * @return this
     */
    public LanguageProfile addTranslationFor(SpellKeyWord keyword, String translation, String... translations)
    {
        if (keyWordMap.containsKey(keyword) && keyWordMap.get(keyword) != null)
        {
            this.keyWordMap.get(keyword).add(translation).addAll(translations);
        }
        else
        {
            this.keyWordMap.put(keyword, TranslatedWords.fromStrings(translations).add(translation));
        }
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Updated translation: " + keyword + "=" + this.keyWordMap.get(keyword));
        return this;
    }

    public LanguageProfile setTranslationFor(ResourceLocation keyWord, final String translation, String... translations)
    {
        return this.setTranslationFor(SpellKeyWord.REGISTRY.getValue(keyWord), translation, translations);
    }

    public LanguageProfile addTranslationFor(ResourceLocation keyWord, String translation, String... translations)
    {
        return this.addTranslationFor(SpellKeyWord.REGISTRY.getValue(keyWord), translation, translations);
    }

    public LanguageProfile setTranslationFor(String keyWord, final String translation, String... translations)
    {
        return this.setTranslationFor(new ResourceLocation(keyWord), translation, translations);
    }

    public LanguageProfile addTranslationFor(String keyWord, String translation, String... translations)
    {
        return this.addTranslationFor(new ResourceLocation(keyWord), translation, translations);
    }

    public String getVariableRegex()
    {
        return "[" + this.leftQuote + "]" + "[^" + this.leftQuote + this.rightQuote +"]*[" + this.rightQuote + "]";
    }

}
