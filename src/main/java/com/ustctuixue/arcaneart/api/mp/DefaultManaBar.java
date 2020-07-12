package com.ustctuixue.arcaneart.api.mp;


import lombok.Getter;
import lombok.Setter;

public class DefaultManaBar implements IManaBar
{
    @Getter @Setter
    private double mana;

    @Getter @Setter
    private int regenCoolDown;

    @Getter @Setter
    private double magicExperience;

    @Getter @Setter
    private int magicLevel;

    /**
     * 消耗 MP 值
     *
     * @param mana 消耗的 MP 量
     */
    @Override
    public void consumeMana(double mana)
    {
        this.mana -= mana;
    }

    /**
     * 增加魔法经验值
     *
     * @param exp 经验值
     */
    @Override
    public void addMagicExperience(double exp)
    {
        this.magicExperience += exp;
    }

    /**
     * 每 tick 调用一次，用于回复冷却倒计时
     *
     * @return 是否可以开始回复
     */
    @Override
    public boolean coolDown()
    {
        return (--regenCoolDown) == 0;
    }
}
