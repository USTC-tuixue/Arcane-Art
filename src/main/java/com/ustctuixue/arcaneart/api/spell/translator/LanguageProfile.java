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
    private final Map<SpellKeyWord, String> keyWordMap = Maps.newHashMap();

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

    private void loadFromRegistry()
    {
        Set<ResourceLocation> keyWords = SpellKeyWord.REGISTRY.getKeys();
        keyWordMap.putAll(
                Maps.asMap(
                        keyWords
                                .stream()
                                .filter(
                                    resourceLocation -> {
                                        SpellKeyWord kw = SpellKeyWord.REGISTRY.getValue(resourceLocation);
                                        return !keyWordMap.containsKey(kw);
                                    }
                                )
                                .map(
                                    (kw)->
                                            SpellKeyWord.REGISTRY.getValue(kw)
                                )
                                .collect(Collectors.toSet()),
                        (k) ->
                        {
                            // I don't know how you registered a null into a Forge registry.
                            Objects.requireNonNull(k);
                            Objects.requireNonNull(k.getRegistryName());
                            return k.getRegistryName().toString();
                        }
                )
        );
    }

    void load(File file)
    {
        CommentedFileConfig config = CommentedFileConfig.of(file);
        config.load();
        loadFromRegistry();
        ArcaneArtAPI.LOGGER.info(LanguageManager.LANGUAGE, "Loading file for language: " + this.name);

        keyWordMap.replaceAll((k, v) -> (String) config.getOptional(k.getTranslationPath()).orElseGet(()->keyWordMap.get(k)));
        this.leftQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.leftQuote").orElse(this.leftQuote);
        this.rightQuote = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.rightQuote").orElse(this.rightQuote);
        this.semicolon = (String) config.getOptional(ArcaneArtAPI.MOD_ID + ".punctuation.period").orElse(this.semicolon);

        for (Map.Entry<SpellKeyWord, String> entry:
                this.keyWordMap.entrySet())
        {
            ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, entry.getKey().getTranslationPath() + " = " + entry.getValue());
            config.add(entry.getKey().getTranslationPath(), entry.getValue());
        }
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.leftQuote", this.leftQuote);
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.rightQuote", this.rightQuote);
        config.add(ArcaneArtAPI.MOD_ID + ".punctuation.period", this.semicolon);
        config.save();
        config.close();
    }

    Pattern getAllPatterns()
    {
        return Pattern.compile(Strings.join(keyWordMap.values(), "|"));
    }

    /**
     *
     * @param sentence 语句
     * @return 翻译后的语句
     */
    public List<String> translate(String sentence)
    {
        String m = sentence;
        for (Map.Entry<SpellKeyWord, String> entry :
                this.keyWordMap.entrySet())
        {
            ResourceLocation rl = entry.getKey().getRegistryName();
            Objects.requireNonNull(rl);

            Pattern pattern = Pattern.compile(entry.getValue());
            m = pattern.matcher(m).replaceAll(rl.toString());
        }
        m = m.replaceAll(this.leftQuote, "\"");
        m = m.replaceAll(this.rightQuote, "\"");
        m = m.replaceAll(this.semicolon, ";");
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Translated: "+m);
        return Arrays.asList(m.split(";"));
    }

    /**
     * This will completely replace default translations
     * @param keyword spell keyword to change
     * @param translation translation word
     * @param translations translation words
     * @return this
     */
    public LanguageProfile setTranslationFor(final SpellKeyWord keyword, final String translation, String... translations)
    {
        if (translations.length != 0)
        {
            this.keyWordMap.put(keyword, String.join("|", translation, String.join("|", translations)));
        }
        else
        {
            this.keyWordMap.put(keyword, translation);
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
        String newTranslations = translation;
        if (translations.length != 0)
        {
            newTranslations = String.join("|", translation, String.join("|", translations));
        }

        if (keyWordMap.containsKey(keyword) && !keyWordMap.get(keyword).isEmpty())
        {
            newTranslations = String.join("|", keyWordMap.get(keyword), newTranslations);
        }
        this.keyWordMap.put(keyword, newTranslations);
        ArcaneArtAPI.LOGGER.debug(LanguageManager.LANGUAGE, "Updated translation: " + keyword + "=" + newTranslations);
        return this;
    }

    public LanguageProfile setTranslationFor(ResourceLocation keyWord, String translation, String... translations)
    {
        return this.setTranslationFor(SpellKeyWord.REGISTRY.getValue(keyWord), translation, translations);
    }

    public LanguageProfile addTranslationFor(ResourceLocation keyWord, String translation, String... translations)
    {
        return this.addTranslationFor(SpellKeyWord.REGISTRY.getValue(keyWord), translation, translations);
    }

    public LanguageProfile setTranslationFor(String keyWord, String translation, String... translations)
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
