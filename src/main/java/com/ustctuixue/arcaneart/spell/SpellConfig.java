package com.ustctuixue.arcaneart.spell;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.udojava.evalex.Expression;
import com.ustctuixue.arcaneart.util.MapSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpellConfig
{

    public SpellConfig(Builder builder)
    {
        builder.push("spell_module");
        SpellProperty.load(builder);
        builder.pop();
    }


    public static class SpellProperty
    {
        public static DoubleValue MANA_COST_AMPLIFIER;
        public static DoubleValue COMPLEXITY_AMPLIFIER;

        static void load(Builder builder)
        {
            builder.push("spell_property");

            MANA_COST_AMPLIFIER = builder
                    .comment(
                            "Global mana cost amplifier, all mana cost will be multiplied by this."
                    )
                    .defineInRange("manaCostaAmplifier", 1.0, 0.0, Double.MAX_VALUE);

            COMPLEXITY_AMPLIFIER = builder
                    .comment(
                            "Global spell complexity amplifier, all complexity will be multiplied by this."
                    )
                    .defineInRange("complexityAmplifier", 1.0, 0.0, Double.MAX_VALUE);

            EffectSpell.load(builder);
            builder.pop();
        }

        public static class EffectSpell
        {
            @AllArgsConstructor
            public static class EffectMagicTraits
            {
                @Getter
                double basicCost;
                @Getter
                double basicComplexity;

                static EffectMagicTraits fromString(String s)
                {
                    String[] list = s.split("\\|", 1);

                    double cost = 0;
                    double complexity = 0;
                    try
                    {
                        cost = Double.parseDouble(list[0]);
                    }catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        complexity = Double.parseDouble(list[1]);
                    }catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }

                    return new EffectMagicTraits(cost, complexity);
                }

                @Override
                public String toString()
                {
                    return basicCost + "|" + basicComplexity;
                }
            }


            private static final Map<Effect, EffectMagicTraits> EFFECT_DEFAULT = Maps.newHashMap();

            static {
                EFFECT_DEFAULT.putAll(
                        ImmutableMap.of(
                                Effects.BLINDNESS, new EffectMagicTraits(50, 1),
                                Effects.HEALTH_BOOST, new EffectMagicTraits(100, 3),
                                Effects.REGENERATION, new EffectMagicTraits(100, 2),
                                Effects.NIGHT_VISION, new EffectMagicTraits(10, 1),
                                Effects.JUMP_BOOST, new EffectMagicTraits(60, 2)
                        )
                );

                EFFECT_DEFAULT.putAll(
                        ImmutableMap.of(
                                Effects.ABSORPTION, new EffectMagicTraits(60D, 4),
                                Effects.SPEED, new EffectMagicTraits(40D, 2),
                                Effects.BLINDNESS, new EffectMagicTraits(150D, 1),
                                Effects.WITHER, new EffectMagicTraits(200D, 5)
                        )
                );
            }

//            public static ConfigValue<Map<Effect, Double>> MANA_COST_BASE;
            private static ConfigValue<String> MANA_COST_AMPLIFIER_EXP;
            private static ConfigValue<String> COMPLEXITY_AMPLIFIER_EXP;
            private static ConfigValue<List<String>> ALLOWED_EFFECTS;

            private static MapSerializer<Effect, EffectMagicTraits> SERIALIZER
                    = new MapSerializer<Effect, EffectMagicTraits>()
                    .withKeySerializer(effect -> Objects.requireNonNull(effect.getRegistryName()).toString())
                    .withValueSerializer(EffectMagicTraits::toString)
                    .withKeyDeserializer(s -> ForgeRegistries.POTIONS.getValue(new ResourceLocation(s)))
                    .withValueDeserializer(EffectMagicTraits::fromString);

            private static Map<Effect, EffectMagicTraits> EFFECTS = null;

            static void load(Builder builder)
            {
                builder.push("effect");

                ALLOWED_EFFECTS = builder
                        .comment(
                                "You can define mana cost and complexity of an effect here",
                                "Format: Effect Id|Mana cost base|Complexity",
                                "e.g.: minecraft:night_vision|10|1.0"
                        )
                        .define("availableEffects", SERIALIZER.serialize(EFFECT_DEFAULT, "|"));

                MANA_COST_AMPLIFIER_EXP = builder
                        .comment(
                                "Mana cost amplifier expression, the total mana cost will be cost base multiplied by this",
                                "Use lvl as level of potion effect (start from 0)",
                                "t as duration time in tick",
                                "n as target number",
                                "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                        )
                        .define("manaCostAmplifier", "((lvl + 1) * t) ^ 2 / 40000 * n");

                COMPLEXITY_AMPLIFIER_EXP = builder
                        .comment(
                                "Spell complexity amplifier expression, the total mana cost will be cost base multiplied by this",
                                "Use lvl as level of potion effect (start from 0)",
                                "t as duration time in tick",
                                "n as target number",
                                "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                        )
                        .define("complexityAmplifier", "(lvl + 1) ^ 3 * 1.1 ^ n");
                builder.pop();
            }

            private static Expression MANA_COST_AMPLIFIER = null;
            private static Expression COMPLEXITY_AMPLIFIER = null;

            public static double getManaCostAmplifier(int lvl, int duration, int n)
            {
                if (MANA_COST_AMPLIFIER == null)
                {
                    MANA_COST_AMPLIFIER = new Expression(MANA_COST_AMPLIFIER_EXP.get());
                }
                return MANA_COST_AMPLIFIER
                        .and("lvl", new BigDecimal(lvl))
                        .and("t", new BigDecimal(duration))
                        .and("n", new BigDecimal(n))
                        .eval().doubleValue();
            }

            public static double getComplexityAmplifier(int lvl, int duration, int n)
            {
                if (COMPLEXITY_AMPLIFIER == null)
                {
                    COMPLEXITY_AMPLIFIER = new Expression(COMPLEXITY_AMPLIFIER_EXP.get());
                }
                return COMPLEXITY_AMPLIFIER
                        .and("lvl", new BigDecimal(lvl))
                        .and("t", new BigDecimal(duration))
                        .and("n", new BigDecimal(n))
                        .eval().doubleValue();
            }

            public static Map<Effect, EffectMagicTraits> getEffectSettings()
            {
                if (EFFECTS == null)
                {
                    EFFECTS = SERIALIZER.deserialize(ALLOWED_EFFECTS.get(), "\\|");
                }
                return EFFECTS;
            }
        }
    }
}
