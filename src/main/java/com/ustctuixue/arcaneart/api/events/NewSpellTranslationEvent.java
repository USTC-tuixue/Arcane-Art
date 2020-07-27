package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

public class NewSpellTranslationEvent extends ModLifecycleEvent
{
    public NewSpellTranslationEvent(ModContainer container)
    {
        super(container);
    }

    public LanguageProfile getProfile(String name)
    {
        return LanguageManager.getInstance().getLanguageProfile(name);
    }
}
