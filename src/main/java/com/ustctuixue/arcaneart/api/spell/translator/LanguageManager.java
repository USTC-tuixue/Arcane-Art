package com.ustctuixue.arcaneart.api.spell.translator;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LanguageManager
{
    private static final String PROFILE_DIR_NAME = "Incantation Languages";
    private static final File PROFILE_DIR = new File(FMLPaths.CONFIGDIR.get().toFile(), PROFILE_DIR_NAME);
    public static Marker LANGUAGE = MarkerManager.getMarker("INCANTATION_LANGUAGES");

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


    // Must run at server start, when registries are frozen
    public void readFromConfig()
    {

        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Generating template for incantation translations");
        generateTemplate();
        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Generating default config for incantation translations");
        Set<String> languages = this.profiles.keySet();

        // Collect config files
        List<File> configFiles = Arrays.stream(Objects.requireNonNull(PROFILE_DIR.listFiles((dir, name) ->
        {
            String[] withExt = name.split("\\.", 2);
            return !withExt[0].equals("empty") && withExt[1].equals("toml");
        }))).collect(Collectors.toList());

        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Found language files:");
        configFiles.forEach(f -> ArcaneArtAPI.LOGGER.info(LANGUAGE, "\t" + f.getName().split("\\.")[0]));
        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Default language pofiles:");
        languages.forEach((language)->ArcaneArtAPI.LOGGER.info(LANGUAGE, "\t" + language));

        if (!languages.isEmpty())
        {
            configFiles.addAll(languages.stream().map(LanguageManager::getFile).collect(Collectors.toSet()));
        }

        ArcaneArtAPI.LOGGER.info(LANGUAGE, "Read from config files");
        for (File configFile : configFiles)
        {
            String[] names = configFile.getName().split("\\.");
            String name = names[0];
            if (!name.equals("empty") && names[1].equals("toml"))
            {
                ArcaneArtAPI.LOGGER.info(LANGUAGE, "Reading language file: " + configFile.getName());
                LanguageProfile profile = getLanguageProfile(name);
                profile.load(getFile(name));
                this.profiles.put(name, profile);
            }
        }
    }

    private static void generateTemplate()
    {
        File templateFile = getFile("empty");
        LanguageProfile profile = new LanguageProfile("empty");
        profile.load(templateFile);
    }


    /**
     *
     * @param sp Spell to detect
     * @return Best matched language profile
     */
    @Nullable
    public LanguageProfile getBestMatchedProfile(String sp)
    {
        ArcaneArtAPI.LOGGER.debug(LANGUAGE, "Matching spell: " + sp);
        Map<String, Integer> counts =
                Maps.transformEntries(profiles, (profileName, profile)->
                {
                    if (profile == null)
                    {
                        return 0;
                    }
                    ArcaneArtAPI.LOGGER.debug(LANGUAGE, "Matching language " + profileName);
                    int matchCount = 0;
                    Pattern pattern = profile.getAllPatterns();
                    ArcaneArtAPI.LOGGER.debug(LANGUAGE, "All Pattern:");
                    ArcaneArtAPI.LOGGER.debug(LANGUAGE, pattern.pattern());
                    Matcher matcher = pattern.matcher(sp);
                    while(matcher.find())
                    {
                        ++matchCount;
                    }
                    ArcaneArtAPI.LOGGER.debug(LANGUAGE, "Matched words count: " + matchCount);
                    return matchCount;
                });
        double maxProb = 0;
        String profileName = null;
        for (Map.Entry<String, Integer> entry: counts.entrySet())
        {
            if (maxProb < entry.getValue())
            {
                maxProb = entry.getValue();
                profileName = entry.getKey();
            }
        }
        return profiles.get(profileName);
    }

    /**
     * Return the language profile according to language name. If the language is not registered, this
     * function will create a new one
     * @param languageName language name
     * @return language profile
     */
    public LanguageProfile getLanguageProfile(String languageName)
    {
        if (!this.profiles.containsKey(languageName))
        {
            this.profiles.put(languageName, new LanguageProfile(languageName));
        }
        return this.profiles.get(languageName);
    }
}
