package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

import java.util.UUID;

public class SpellCasterTiers
{
    public static final int MAX_TIER = 999;
    public static final int MIN_TIER = -999;

    public static final IAttribute CASTER_TIER
            = new RangedAttribute(null, "arcaneart.caster_tier",
            0.0, MIN_TIER, MAX_TIER
    );

    public static final int PLAYER = 0;
    public static final int INSTRUMENT = 100;
    public static final int RITUAL = 200;


}
