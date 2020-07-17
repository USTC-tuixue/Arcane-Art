package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import net.minecraftforge.registries.IForgeRegistry;

public class SpellKeyWords
{
    private static SpellKeyWord register(String name)
    {
        return new SpellKeyWord().setRegistryName(ArcaneArtAPI.getResourceLocation(name));
    }

    public static final SpellKeyWord AT = register("at");
    public static final SpellKeyWord TOWARDS = register("towards");
    public static final SpellKeyWord NEAREST_ENTITY = register("nearest_entity");
    public static final SpellKeyWord NEAREST_ENEMY = register("nearest_enemy");

    public static void registerAll(IForgeRegistry<SpellKeyWord> registry)
    {
        registry.register(AT);
        registry.register(TOWARDS);
        registry.register(NEAREST_ENTITY);
        registry.register(NEAREST_ENEMY);
    }
}
