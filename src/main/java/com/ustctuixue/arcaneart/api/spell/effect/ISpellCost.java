package com.ustctuixue.arcaneart.api.spell.effect;

public interface ISpellCost
{
    /**
     *
     * @return 法术执行一次所需的魔法，持续性法术每刻执行一次
     */
    double manaCost();
}
