package com.ustctuixue.arcaneart.api.spell.translator;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageManager
{
    private static final String PROFILE_DIR_NAME = "Incantation Languages";
    private static final File PROFILE_DIR = new File(FMLPaths.CONFIGDIR.get().toFile(), PROFILE_DIR_NAME);

    private LanguageManager()
    {}

    private static final LanguageManager INSTANCE = new LanguageManager();

    public static LanguageManager getInstance()
    {
        return INSTANCE;
    }

    private final Map<String, LanguageProfile> profiles = Maps.newHashMap();

    /**
     *
     * @param languageName name for language
     * @return file for the language
     */
    private static File getFile(String languageName)
    {
        if (!PROFILE_DIR.exists())
        {
            if (!PROFILE_DIR.mkdirs())
            {
                ArcaneArt.LOGGER.error("Cannot create directory at " + PROFILE_DIR);
                System.exit(1); // I just wanted to create a directory but failed! No way!
            }
        }
        return new File(PROFILE_DIR, languageName + ".toml");
    }
    static Marker LANGUAGE = MarkerManager.getMarker("INCANTATION_LANGUAGES");
    // Must run at server start, when registries are frozen
    public void readFromConfig()
    {

        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Generating template for incantation translations");
        generateTemplate();
        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Generating default config for incantation translations");
        Set<String> languages = Sets.newHashSet();
        for (SpellKeyWord keyWord :
                SpellKeyWord.REGISTRY)
        {
            languages.addAll(keyWord.getDefaultTranslationKeySet());
        }
        Set<File> configFiles =
                languages.stream().map(LanguageManager::getFile).collect(Collectors.toSet());
        configFiles.addAll(Arrays.asList(Objects.requireNonNull(PROFILE_DIR.listFiles((dir, name) ->
        {
            String[] withExt = name.split("\\.", 2);
            return !withExt[0].equals("empty") && withExt[1].equals("toml");
        }))));

        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Read from config files");
        for (File configFile :
                Objects.requireNonNull(PROFILE_DIR.listFiles()))
        {
            String[] names = configFile.getName().split("\\.");
            String name = names[0];
            if (!name.equals("empty") && names[1].equals("toml"))
            {
                ArcaneArtAPI.LOGGER.info(LANGUAGE, "Reading language file: " + configFile.getName());
                LanguageProfile profile = new LanguageProfile(name,
                        SpellKeyWord.REGISTRY.getKeys()
                );
                profile.load(getFile(name));
                this.profiles.put(name, profile);
            }
        }
    }

    private static void generateTemplate()
    {
        File templateFile = getFile("empty");
        CommentedFileConfig config = CommentedFileConfig.of(templateFile);
        SpellKeyWord.REGISTRY.getValues().forEach(
                (keyWord) -> config.add(keyWord.getTranslationPath(), "")
        );
        config.save();
        config.close();
    }


    public LanguageProfile getBestMatchedProfile(List<String> sp)
    {
        Map<String, Double> probabilities =
                Maps.transformEntries(profiles, (profileName, profile)->
                {
                    String[] words = sp.get(0).split("\\s");
                    double matchCount = 0;
                    int varCount = 0;
                    if (Objects.isNull(profile))
                    {
                        ArcaneArtAPI.LOGGER.error(LANGUAGE, "Language Profile \"" + profileName + "\" is null!");
                        return 0.;
                    }
                    for (String word : words)
                    {
                        if (word.matches(profile.getAllPatterns()))
                        {
                            matchCount += 1;
                        }
                        else if (word.matches(APIConfig.Spell.getVariableRegex()))
                        {
                            varCount++;
                        }

                    }
                    return  matchCount / (words.length - varCount);
                });
        double maxProb = 0;
        String profileName = null;
        for (Map.Entry<String, Double> entry: probabilities.entrySet())
        {
            if (maxProb < entry.getValue())
            {
                maxProb = entry.getValue();
                profileName = entry.getKey();
            }
        }
        return profiles.get(profileName);
    }
}
