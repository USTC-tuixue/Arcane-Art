package com.ustctuixue.arcaneart.api.mp.tile;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.MathHelper;

public class MPStorage
{
    @Getter
    double mana;

    @Getter @Setter
    double maxMP;

    @Getter @Setter
    double inputRateLimit;

    @Getter @Setter
    double outputRateLimit;

    public void setMana(double mana)
    {
        this.mana = MathHelper.clamp(mana, 0, maxMP);
    }

}
