package com.ustctuixue.arcaneart.spell;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

public class SpellModuleRegistries
{
    public static class SpellKeyWords
    {
        public static final DeferredRegister<SpellKeyWord> SPELL_KEY_WORD_DEFERRED_REGISTER
                = new DeferredRegister<>(SpellKeyWord.REGISTRY, ArcaneArt.MOD_ID);

        public static final SpellKeyWord GRAB
                = new SpellKeyWord
                (
                        new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_HOLD)
                )
                .setRegistryName(ArcaneArt.getResourceLocation("grab"));

        public static final SpellKeyWord SUMMON
                = new SpellKeyWord
                (
                        new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_RELEASE)
                )
                .setRegistryName(ArcaneArt.getResourceLocation("summon"));

        public static final SpellKeyWord EFFECT
                = new SpellKeyWord
                (
                        new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_RELEASE)
                )
                .setRegistryName(ArcaneArt.getResourceLocation("effect"));

        @SubscribeEvent
        public void registerWords(RegistryEvent.Register<SpellKeyWord> event)
        {
            event.getRegistry().register(EFFECT);
        }
    }

    public static class SpellTranslations
    {
        // public static LanguageProfile LATIN = LanguageManager.getInstance().getLanguageProfile("latin");
        public static LanguageProfile EN_US = LanguageManager.getInstance().getLanguageProfile("en_us");
        @SubscribeEvent
        public void addTranslations(FMLCommonSetupEvent event)
        {
            EN_US.addTranslationFor(SpellKeyWords.EFFECT, "effect");
        }
    }
}
