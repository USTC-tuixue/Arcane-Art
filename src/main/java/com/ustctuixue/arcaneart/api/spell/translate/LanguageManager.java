package com.ustctuixue.arcaneart.api.spell.translate;

import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LanguageManager
{
    public static final String PROFILE_DIR_NAME = "Incantation Languages";
    public static final File PROFILE_DIR = new File(FMLPaths.CONFIGDIR.get().toFile(), PROFILE_DIR_NAME);

    LanguageManager()
    {}

    static final LanguageManager INSTANCE = new LanguageManager();

    public static LanguageManager getInstance()
    {
        return INSTANCE;
    }

    private final Map<String, LanguageProfile> profiles = Maps.newHashMap();

    /**
     *
     * @param languageName
     * @return
     */
    public static File getFile(String languageName)
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
        for (File configFile :
                Objects.requireNonNull(PROFILE_DIR.listFiles()))
        {
            String name = configFile.getName().split("\\.")[0];
            LanguageProfile profile = new LanguageProfile(name,
                    SpellKeyWord.REGISTRY.getKeys()
            );
            profile.load(getFile(name));
            this.profiles.put(name, profile);
        }
    }

    public LanguageProfile getBestMatchedProfile(List<String> sp)
    {
        Map<String, Double> probabilities =
                Maps.transformEntries(profiles, (profileName, profile)->
                {
                    String[] words = sp.get(0).split("\\s");
                    double matchCount = 0;
                    int varCount = 0;
                    for (String word : words)
                    {
                        Objects.requireNonNull(profile);
                        if (word.matches(profile.getAllPatterns()))
                        {
                            matchCount += 1;
                        }
                        else if (word.matches("[\"“「『][^\"”]*[\"”」』]"))
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
