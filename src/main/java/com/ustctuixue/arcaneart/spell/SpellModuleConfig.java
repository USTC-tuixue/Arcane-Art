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

public class SpellModuleConfig
{

    public SpellModuleConfig(Builder builder)
    {
        builder.push("spell_module");
        SpellProperty.load(builder);
        SpellModifiers.load(builder);
        builder.pop();
    }


    public static class SpellProperty
    {


        public static final GrabSpell GRAB_SPELL_CONFIG = new GrabSpell();

        static void load(Builder builder)
        {
            builder.push("spell_property");


            EffectSpell.load(builder);
            GRAB_SPELL_CONFIG.load(builder);
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
                    String[] list = s.split("\\|", 2);

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

            private static ConfigValue<String> MANA_COST_AMPLIFIER_EXP;
            private static ConfigValue<String> COMPLEXITY_AMPLIFIER_EXP;
            private static ConfigValue<List<String>> ALLOWED_EFFECTS;

            private static MapSerializer<Effect, EffectMagicTraits> SERIALIZER
                    = new MapSerializer<Effect, EffectMagicTraits>()
                    .withKeySerializer(effect -> Objects.requireNonNull(effect.getRegistryName()).toString())
                    .withValueSerializer(EffectMagicTraits::toString)
                    .withKeyDeserializer(s -> ForgeRegistries.POTIONS.getValue(new ResourceLocation(s.split("\\|")[0])))
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
                        .define("manaCostAmplifier", "((lvl + 1) * t) ^ 2 / 400000 * n");

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

        public static class GrabSpell extends SpellConfig
        {

            public GrabSpell()
            {
                super("grab");
                this.defaultAmpExp = "n ^ 1.5 * (1 + d)";
                this.defaultBaseComplexity = 10;
                this.defaultBaseManaCost = 50;
                this.defaultCmxExp = "n ^ 1.1 * (1 + d)";
                this.variantComment = new String[]{
                        "n refers to number of targets",
                        "d refers to distance"
                };
            }

            public double getManaCostAmp(int n, double distance)
            {
                return getManaCostAmplifier()
                        .and("n", new BigDecimal(n))
                        .and("d", new BigDecimal(distance))
                        .eval().doubleValue();
            }

            public double getComplexityAmp(int n, double distance)
            {
                return getComplexityAmplifier()
                        .and("n", new BigDecimal(n))
                        .and("d", new BigDecimal(distance))
                        .eval().doubleValue();
            }

        }
    }

    public static class SpellModifiers
    {
        private static ConfigValue<String> ARMOR_ENCHANTABILITY_MODIFIER;
        private static Expression ARMOR_ENCHANTABILITY_MODIFIER_EXP = null;

        public static DoubleValue ARMOR_ENCHANTABILITY_DEFAULT;

        static void load(Builder builder)
        {
            builder.push("modifier");

            ARMOR_ENCHANTABILITY_MODIFIER = builder
                    .comment(
                            "Armor enchantability modifier expression",
                            "Mana cost for spells will be multiplied by this",
                            "Use 'ench' refers to average armor enchantability weighed by max durability.",
                            "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                    )
                    .define("armorEnchantabilityModifier", "15 / ench");

            ARMOR_ENCHANTABILITY_DEFAULT = builder
                    .comment(
                            "Default average enchantability for players have no available armor in their equipment slots"
                    )
                    .defineInRange("defaultArmorEnchantability", 15D, 0D, Double.MAX_VALUE);

            builder.pop();
        }

        public static double getArmorEnchantabilityModifier(double ench)
        {
            if (ARMOR_ENCHANTABILITY_MODIFIER_EXP == null)
            {
                ARMOR_ENCHANTABILITY_MODIFIER_EXP = new Expression(ARMOR_ENCHANTABILITY_MODIFIER.get());
            }

            double r = ARMOR_ENCHANTABILITY_MODIFIER_EXP
                    .and("ench", BigDecimal.valueOf(ench))
                    .eval().doubleValue();

            return r > 0? r : 1;
        }
    }
}
