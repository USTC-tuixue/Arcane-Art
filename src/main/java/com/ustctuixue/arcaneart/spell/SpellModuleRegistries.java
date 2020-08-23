package com.ustctuixue.arcaneart.spell;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.events.NewSpellEvent;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.spell.spell.EffectSpell;
import com.ustctuixue.arcaneart.spell.spell.GrabSpell;
import com.ustctuixue.arcaneart.spell.spell.SummonSpellBallSpell;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

public class SpellModuleRegistries
{
    public static class SpellKeyWords
    {
        public static final DeferredRegister<SpellKeyWord> SPELL_KEY_WORD_DEFERRED_REGISTER
                = new DeferredRegister<>(SpellKeyWord.REGISTRY, ArcaneArt.MOD_ID);

        public static final RegistryObject<SpellKeyWord> GRAB
                = SPELL_KEY_WORD_DEFERRED_REGISTER.register("grab", () ->
                new SpellKeyWord
                (
                        new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_HOLD)
                )
                .setRegistryName(ArcaneArt.getResourceLocation("grab")));

        public static final RegistryObject<SpellKeyWord> SUMMON
                = SPELL_KEY_WORD_DEFERRED_REGISTER.register("summon", () ->
                new SpellKeyWord
                (
                        new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_RELEASE)
                )
                .setRegistryName(ArcaneArt.getResourceLocation("summon")));

        public static final RegistryObject<SpellKeyWord> EFFECT
                = SPELL_KEY_WORD_DEFERRED_REGISTER.register("effect",
                new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.ON_RELEASE).getSupplier()
        );

        public static final RegistryObject<SpellKeyWord> WITH_SPEED
                = SPELL_KEY_WORD_DEFERRED_REGISTER.register("with_speed",
                new SpellKeyWord.Property().withType(SpellKeyWord.ExecuteType.NOT_EXECUTABLE).getSupplier());

    }

    public static class SpellTranslations
    {
        // public static LanguageProfile LATIN = LanguageManager.getInstance().getLanguageProfile("latin");
        static LanguageProfile EN_US = LanguageManager.getInstance().getLanguageProfile("en_us");
        @SubscribeEvent
        public void addTranslations(FMLCommonSetupEvent event)
        {
            EN_US.addTranslationFor(SpellKeyWords.EFFECT.get(), "effect");
            EN_US.addTranslationFor(SpellKeyWords.GRAB.get(), "grab");
            EN_US.addTranslationFor(SpellKeyWords.SUMMON.get(), "summon");
            EN_US.addTranslationFor(SpellKeyWords.WITH_SPEED.get(), "with speed");
        }
    }

    @SubscribeEvent
    public void registerSpells(NewSpellEvent event)
    {
        event.register(SpellKeyWords.EFFECT.get(), EffectSpell::new);
        event.register(SpellKeyWords.GRAB.get(), GrabSpell::new);
        event.register(SpellKeyWords.SUMMON.get(), SummonSpellBallSpell::new);
    }
}
