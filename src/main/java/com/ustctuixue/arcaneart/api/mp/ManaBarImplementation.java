package com.ustctuixue.arcaneart.api.mp;


public class ManaBarImplementation implements IManaBar
{
    private double mana;

    private double regenRate;

    private double experience;
    private int level;

    /**
     * @return 获取生物当前 MP 值
     */
    @Override
    public double getMana()
    {
        return mana;
    }

    /**
     * 设置生物当前的 MP 值
     *
     * @param mana 设置的值，如果小于 0 则认为是 0
     */
    @Override
    public void setMana(double mana)
    {
        this.mana = mana;
    }

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
     * @return 返回 MP 回复速度，单位为 MP / tick
     */
    @Override
    public double getRegenRate()
    {
        return this.regenRate;
    }

    /**
     * 获取当前魔法经验值，升级时清零
     *
     * @return 魔法经验
     */
    @Override
    public double getMagicExperience()
    {
        return this.experience;
    }

    /**
     * 设置当前的魔法经验值
     *
     * @param exp 经验值
     */
    @Override
    public void setMagicExperience(double exp)
    {
        this.experience = exp;
    }

    /**
     * 增加魔法经验值
     *
     * @param exp 经验值
     */
    @Override
    public void addMagicExperience(double exp)
    {
        this.experience += exp;
    }

    /**
     * 获得当前的魔法等级
     *
     * @return 魔法等级
     */
    @Override
    public int getMagicLevel()
    {
        return this.level;
    }

    /**
     * 设置魔法等级
     *
     * @param level 等级
     */
    @Override
    public void setMagicLevel(int level)
    {
        this.level = level;
    }

    /**
     * 设置 MP 回复速度
     *
     * @param rate 单位为 MP / tick
     */
    @Override
    public void setRegenRate(double rate)
    {
        this.regenRate = rate;
    }

}
