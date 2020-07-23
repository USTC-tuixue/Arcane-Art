package com.ustctuixue.arcaneart.api.mp;

public interface IMPConsumer
{
    /**
     * 消耗 MP 值
     * @param manaIn 消耗的 MP 量
     * @return 有足够的 MP 供消耗则返回 true，否则为 false
     */
    boolean consumeMana(double manaIn);
}
