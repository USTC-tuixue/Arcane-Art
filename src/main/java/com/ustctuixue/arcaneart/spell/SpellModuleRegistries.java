package com.ustctuixue.arcaneart.spell;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.Spells;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListVariableArgument;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.util.EntityList;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;

public class SpellModuleRegistries
{
    public static class SpellKeyWords
    {
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

    public static class SpellRegister
    {
        public static class GiveEffect
        {
            public static Map<Effect, Double> EFFECT_COST = ImmutableMap.of(
                    Effects.NIGHT_VISION, 100D
            );

            GiveEffect(SpellDispatcher dispatcher)
            {
                dispatcher.register
                        (
                                Spells.literal(SpellKeyWords.EFFECT)
                                        .then(
                                                Spells.argument("target", new EntityListVariableArgument())
                                                .then(
                                                        Spells.argument("effect", new PotionArgument())
                                                        .executes(context ->
                                                                giveEffect(context.getSource(), context.getArgument("target", EntityList.class), context.getArgument("effect", Effect.class))
                                                        )
                                                )
                                        )
                        );
            }

            static int giveEffect(SpellCasterSource source, EntityList targets, Effect effect)
            {
                int result = 0;
                for (Entity entity :
                        targets)
                {
                    if (entity.isLiving())
                    if (source.getMpConsumer().consumeMana(EFFECT_COST.getOrDefault(effect, 50D)))
                    {
                        ((LivingEntity) entity).addPotionEffect(new EffectInstance(effect));
                        result ++;
                    }
                }
                return result;
            }



        }
    }
}
