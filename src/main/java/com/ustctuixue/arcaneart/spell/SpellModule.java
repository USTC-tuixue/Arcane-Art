package com.ustctuixue.arcaneart.spell;

import com.ustctuixue.arcaneart.api.util.Module;

public class SpellModule extends Module
{
    @Override
    protected Object[] getModLoadingEventHandler()
    {
        return new Object[]{
                new SpellModuleRegistries.SpellKeyWords()
        };
    }

    @Override
    protected Object[] getCommonEventHandler()
    {
        return new Object[]{
                new SpellModuleRegistries.SpellTranslations()
        };
    }
}
