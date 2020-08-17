package com.ustctuixue.arcaneart.api;

import com.udojava.evalex.Expression;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.math.BigDecimal;

public class APIConfig
{
    public APIConfig(ForgeConfigSpec.Builder builder)
    {
        builder.push("api");
        MP.load(builder);
        Spell.load(builder);
        builder.pop();
    }

    public static class MP
    {
        public static ForgeConfigSpec.DoubleValue DEATH_RESET_RATIO;
        public static ForgeConfigSpec.DoubleValue MANA_COST_AMPLIFIER;
        public static ForgeConfigSpec.DoubleValue COMPLEXITY_AMPLIFIER;

        static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("MP");

            DEATH_RESET_RATIO = builder
                    .comment("After death, players' MP will be reset to this value multiply by players' max MP.")
                    .defineInRange("AfterDeathResetRatio", 0.25, 0.0, 1.0);

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


            Regeneration.load(builder);
            Levelling.load(builder);
            builder.pop();
        }

        public static class Regeneration
        {
            public static ForgeConfigSpec.IntValue COOLDOWN_TICK;
            public static ForgeConfigSpec.BooleanValue ENABLE;
            public static ForgeConfigSpec.DoubleValue REGEN_RATE;

            static void load(ForgeConfigSpec.Builder builder)
            {

                builder.push("Regeneration");

                ENABLE = builder
                        .comment(
                                "Enable regeneration.",
                                "If false, players' MP will not regenerate naturally, " +
                                        "but still able to get refilled by having potions"
                        )
                        .define("EnableRegeneration", true);
                COOLDOWN_TICK = builder
                        .comment(
                                "MP regeneration cooldown in ticks."
                        )
                        .defineInRange("RegenCooldownTick", 100, 0, 10000);

                REGEN_RATE = builder
                        .comment("Regeneration rate factor, 1.0 means ")
                        .defineInRange("RegenerationRate", 1.0D, 0.0D, 1000.0D);

                builder.pop();

            }
        }

        public static class Levelling
        {

            private static ForgeConfigSpec.ConfigValue<String> MAX_MP_CURVE;
            private static Expression MAX_MP_EXP;

            public static ForgeConfigSpec.IntValue MAX_LEVEL;

            private static ForgeConfigSpec.ConfigValue<String> RAW_EXP_CURVE;
            private static Expression EXP_CURVE = null;

            private static ForgeConfigSpec.ConfigValue<String> COMPLEXITY_TOLERANCE_CURVE;
            private static Expression COMPLEXITY_TOLERANCE_EXP;

            static void load(ForgeConfigSpec.Builder builder)
            {
                builder.push("Levelling");

                MAX_MP_CURVE = builder
                        .comment(
                                "Max MP for each level",
                                "Use lvl for current magic level",
                                "Result is base value of max MP",
                                "Note: level starts from 0",
                                "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                        )
                        .define("MaxMp", "(lvl + 1) * 100");

                MAX_LEVEL = builder
                        .comment("Max level players can achieve.")
                        .defineInRange("MaxLevel", 10, 0, 32767);

                RAW_EXP_CURVE = builder
                        .comment(
                                "Experience curve for levelling. ",
                                "Use lvl to refer to current magic level.",
                                "Result is the experience needed to level up.",
                                "Experience across levels is not cumulative.",
                                "Note: level starts from 0 .",
                                "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                        )
                        .define("ExpCurve", "(lvl + 1) ^ 2 * 4000");

                COMPLEXITY_TOLERANCE_CURVE = builder
                        .comment(
                                "Max complexity for spells player can cast",
                                "Use lvl to refer to current magic level",
                                "Note: level starts from 0 .",
                                "Expression Usage: https://github.com/uklimaschewski/EvalEx"
                        )
                        .define("ComplexityToleranceCurve", "10 + lvl ^ 2");

                builder.pop();
            }



            public static double getExpToNextLvl(int currentLevel)
            {
                if (EXP_CURVE == null)
                {
                    EXP_CURVE = new Expression(RAW_EXP_CURVE.get());
                }
                return EXP_CURVE.and("lvl", BigDecimal.valueOf(currentLevel))
                        .eval().doubleValue();
            }

            public static double getMaxMPForCurrentLevel(int currentLevel)
            {
                if (MAX_MP_EXP == null)
                {
                    MAX_MP_EXP = new Expression(MAX_MP_CURVE.get());
                }
                return MAX_MP_EXP
                        .and("lvl", BigDecimal.valueOf(currentLevel))
                        .eval().doubleValue();
            }

            public static double getComplexityTolerance(int currentLevel)
            {
                if (COMPLEXITY_TOLERANCE_EXP == null)
                {
                    COMPLEXITY_TOLERANCE_EXP = new Expression(COMPLEXITY_TOLERANCE_CURVE.get());
                }
                return COMPLEXITY_TOLERANCE_EXP
                        .and("lvl", BigDecimal.valueOf(currentLevel))
                        .eval().doubleValue();
            }
        }
    }

    public static class Spell
    {
        public static ForgeConfigSpec.IntValue MAX_LIFE_TIME;
        public static ForgeConfigSpec.DoubleValue DESCENDING_RATE;

        static void load(ForgeConfigSpec.Builder builder)
        {

            builder.push("SpellBall");

            MAX_LIFE_TIME = builder
                    .comment(
                            "An spell ball will lose MP after this time"
                    )
                    .defineInRange("MaxLifeTime", 1000, 0, 100000);

            DESCENDING_RATE = builder
                    .comment("An spell ball will lose MP after MaxLifeTime with this speed per tick")
                    .defineInRange("DescendingRate", 0.02D, 0.0D, 1.0D);

            builder.pop();

        }
    }


}
