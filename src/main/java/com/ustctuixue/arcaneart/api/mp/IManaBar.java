package com.ustctuixue.arcaneart.api.mp;


public interface IManaBar
{
    double getMaxManaBase();
    double getMana();
    void setMana(double mana);
    double getMaxMana();

    void consumeMana(double mana);

    double getRegenRate();
    void setRegenRate();

}
