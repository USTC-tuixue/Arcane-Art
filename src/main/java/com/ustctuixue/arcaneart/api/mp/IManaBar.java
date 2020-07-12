package com.ustctuixue.arcaneart.api.mp;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public interface IManaBar
{
    IAttribute MAGIC_LEVEL =
            new RangedAttribute(null, "arcaneart.magicLevel",
                    1.0D, 0.0D, 10.0D)
            .setShouldWatch(true);

    IAttribute MAX_MANA = new RangedAttribute(null, "arcaneart.maxMana",
            100.0D, 0.0D, 1000.0D);

}
