package com.ustctuixue.arcaneart.api.mp.tile;

import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.MathHelper;

public class MPStorage implements IMPConsumer
{
    @Getter
    double mana;

    @Getter @Setter
    double maxMP;

    /*
    @Getter @Setter
    double inputRateLimit;

    @Getter @Setter
    double outputRateLimit;
    */
    //由于无法做出好的全局单tick mana输入输出锁，删去本属性值

    public void setMana(double mana)
    {
        this.mana = MathHelper.clamp(mana, 0, maxMP);
    }

    /**
     * 消耗 MP 值
     *
     * @param manaIn 消耗的 MP 量
     * @return 有足够的 MP 供消耗则返回 true，否则为 false
     */
    @Override
    public boolean consumeMana(double manaIn)
    {
        if (manaIn > this.getMana())
            return false;
        else
        {
            this.setMana(this.mana - manaIn);
            return true;
        }
    }
}
