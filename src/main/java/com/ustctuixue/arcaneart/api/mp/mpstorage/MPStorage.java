package com.ustctuixue.arcaneart.api.mp.mpstorage;

import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

public class MPStorage implements IMPConsumer, INBTSerializable<CompoundNBT>
{
    static String MANA = "mana";
    static String MAX_MANA = "max_mana";

    @Getter
    double mana;

    @Getter @Setter
    double maxMana;

    public void setMana(double mana)
    {
        this.mana = MathHelper.clamp(mana, 0, maxMana);
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putDouble(MANA, mana);
        compound.putDouble(MAX_MANA, maxMana);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.mana = nbt.getDouble(MANA);
        this.maxMana = nbt.getDouble(MAX_MANA);
    }
}
