package com.ustctuixue.arcaneart.api.mp;


public interface IManaBar
{
    /**
     * @return 获取生物当前 MP 值
     */
    double getMana();

    /**
     * 设置生物当前的 MP 值，不检查 MP 值是否超出上限
     * @param mana 设置的值，如果小于 0 则认为是 0
     */
    void setMana(double mana);


    /**
     * 消耗 MP 值
     * @param mana 消耗的 MP 量
     */
    void consumeMana(double mana);

    /**
     *
     * @return 返回 MP 回复冷却时间，单位为 tick
     */
    int getRegenCoolDown();

    /**
     * 设置 MP 回复冷却时间
     * @param coolDown 单位为 tick
     */
    void setRegenCoolDown(int coolDown);

    /**
     * 每 tick 调用一次，用于回复冷却倒计时
     * @return 是否可以开始回复
     */
    boolean coolDown();


    /**
     * 获取当前魔法经验值，升级时清零
     * @return 魔法经验
     */
    double getMagicExperience();

    /**
     * 设置当前的魔法经验值
     * @param exp 经验值
     */
    void setMagicExperience(double exp);

    /**
     * 增加魔法经验值
     * @param exp 经验值
     */
    void addMagicExperience(double exp);

    /**
     * 获得当前的魔法等级
     * @return 魔法等级
     */
    int getMagicLevel();

    /**
     * 设置魔法等级
     * @param level 等级
     */
    void setMagicLevel(int level);
}
