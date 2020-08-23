package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import net.minecraftforge.registries.IForgeRegistry;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord.Property;

public class SpellKeyWords
{
    private static SpellKeyWord create(String name, Property property)
    {
        return new SpellKeyWord(property).setRegistryName(ArcaneArtAPI.getResourceLocation(name));
    }

    private static void register(IForgeRegistry<SpellKeyWord> registry, SpellKeyWord... words)
    {
        for (SpellKeyWord word : words)
        {
            registry.register(word);
        }
    }

    private static SpellKeyWord create(String name)
    {
        return create(name, new Property().withType(SpellKeyWord.ExecuteType.NOT_EXECUTABLE));
    }

    public static final SpellKeyWord AT = create("at");
    public static final SpellKeyWord TOWARDS = create("towards");
    public static final SpellKeyWord NEAREST_ENTITY = create("nearest_entity");
    public static final SpellKeyWord NEAREST_ENEMY = create("nearest_enemy");
    public static final SpellKeyWord NEAREST_ANIMAL = create("nearest_animal");
    public static final SpellKeyWord NEAREST_PLAYER = create("nearest_player");
    public static final SpellKeyWord NEAREST_PROJECTILE = create("nearest_projectile");
    public static final SpellKeyWord NEAREST_ITEM = create("nearest_item");
    public static final SpellKeyWord NEAREST_ALLY = create("ally");
    public static final SpellKeyWord IN_RANGE = create("in_range");
    public static final SpellKeyWord FROM = create("from");
    public static final SpellKeyWord MAX_COUNT = create("with_max_count");
    public static final SpellKeyWord SELF = create("self");

    public static final SpellKeyWord MAKE = create("make", new Property().withType(SpellKeyWord.ExecuteType.PRE_PROCESS));

    public static void registerAll(IForgeRegistry<SpellKeyWord> registry)
    {
        register
                (
                        registry,
                        AT, TOWARDS, NEAREST_ENEMY, NEAREST_ENTITY, NEAREST_ITEM,
                        NEAREST_ANIMAL, NEAREST_PLAYER, NEAREST_PROJECTILE, MAKE, SELF
                );
    }

    public static final LanguageProfile EN_US = LanguageManager.getInstance().getLanguageProfile("en_us");

    public static void addAllTranslations()
    {
        EN_US.addTranslationFor(AT, "at", "on");
        EN_US.addTranslationFor(TOWARDS, "towards");
        EN_US.addTranslationFor(NEAREST_ENTITY, "nearest entity");
        EN_US.addTranslationFor(MAKE, "make");
        EN_US.addTranslationFor(NEAREST_ENEMY, "nearest enemy");
        EN_US.addTranslationFor(NEAREST_ALLY, "nearest ally");
        EN_US.addTranslationFor(NEAREST_ANIMAL, "nearest animal");
        EN_US.addTranslationFor(NEAREST_PLAYER, "nearest player");
        EN_US.addTranslationFor(NEAREST_ITEM, "nearest item");
        EN_US.addTranslationFor(NEAREST_PROJECTILE, "nearest projectile");
        EN_US.addTranslationFor(IN_RANGE, "in range");
        EN_US.addTranslationFor(FROM, "from");
        EN_US.addTranslationFor(MAX_COUNT, "max count");
        EN_US.addTranslationFor(SELF, "self");
        EN_US.addTranslationFor(MAKE, "make");
        EN_US.setLeftQuote("\"");
        EN_US.setRightQuote("\"");
        EN_US.setSemicolon(";");
    }
}
