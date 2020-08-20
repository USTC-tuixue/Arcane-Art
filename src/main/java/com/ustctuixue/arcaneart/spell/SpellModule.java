package com.ustctuixue.arcaneart.spell;

import com.ustctuixue.arcaneart.api.util.Module;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpellModule extends Module
{
    @Override
    protected Object[] getModLoadingEventHandler()
    {
        return new Object[]{
                new SpellModuleRegistries()
        };
    }

    @Override
    protected Object[] getCommonEventHandler()
    {
        return new Object[]{
                new SpellModuleRegistries.SpellTranslations(),
                new SpellModuleRegistries()
        };
    }

    @Override @OnlyIn(Dist.CLIENT)
    protected Object[] getClientEventHandler()
    {
        return new Object[0];
    }
}
