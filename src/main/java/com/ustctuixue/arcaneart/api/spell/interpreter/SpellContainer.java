package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.google.common.collect.Lists;

import java.util.List;

public class SpellContainer
{
    public final List<ISpell> preProcess = Lists.newArrayList();
    public final List<ISpell> onHold = Lists.newArrayList();
    public final List<ISpell> onRelease = Lists.newArrayList();

    public boolean isEmpty()
    {
        return preProcess.isEmpty() && onHold.isEmpty() && onRelease.isEmpty();
    }

    public void executePreProcess(SpellCasterSource source)
    {
        preProcess.forEach(iSpell -> iSpell.execute(source));
    }

    public void executeOnHold(SpellCasterSource source)
    {
        onHold.forEach(iSpell -> iSpell.execute(source));
    }

    public void executeOnRelease(SpellCasterSource source)
    {
        onRelease.forEach(iSpell -> iSpell.execute(source));
    }
}
