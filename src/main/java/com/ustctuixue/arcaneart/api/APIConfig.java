package com.ustctuixue.arcaneart.api;

import com.udojava.evalex.Expression;
import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class APIConfig
{

    public static final ForgeConfigSpec API_CONFIG =
            new ForgeConfigSpec.Builder().configure(APIConfig::new).getRight();

    APIConfig(ForgeConfigSpec.Builder builder)
    {
        MP.load(builder);
    }

    public static class MP
    {
        public static ForgeConfigSpec.DoubleValue DEATH_RESET_RATIO;

        public static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("MP");

            DEATH_RESET_RATIO = builder
                    .comment("After death, players' MP will be reset to this value multiply by players' max MP.")
                    .defineInRange("AfterDeathResetRatio", 0.25, 0.0, 1.0);

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

            public static ForgeConfigSpec.DoubleValue INITIAL_MAX_MP;
            public static ForgeConfigSpec.DoubleValue ADDITIONAL_MP_PER_LEVEL;
            public static ForgeConfigSpec.IntValue MAX_LEVEL;

            private static ForgeConfigSpec.ConfigValue<String> RAW_EXP_CURVE;
            public static Expression EXP_CURVE;

            public static void load(ForgeConfigSpec.Builder builder)
            {
                builder.push("Levelling");

                INITIAL_MAX_MP = builder
                        .comment("Initial max MP for LV.0 players")
                        .defineInRange("InitialMaxMP", 100.0D, 0.0D, InnerNumberDefaults.MAX_ALLOWED_MP);

                ADDITIONAL_MP_PER_LEVEL = builder
                        .comment("Max MP will increase by this when levelling up")
                        .defineInRange("AdditionalMPPerLevel", 100.0D, 0.0D, InnerNumberDefaults.MAX_ALLOWED_MP);

                MAX_LEVEL = builder
                        .comment("Max level players can achieve.")
                        .defineInRange("MaxLevel", 0, 0, 32767);

                RAW_EXP_CURVE = builder
                        .comment(
                                "Experience curve for levelling. ",
                                "Use x refer to current magic level.",
                                "Result is the experience needed to level up.",
                                "Experience across levels is not cumulative.",
                                "Note that level starts at LV.0 ."
                        )
                        .define("ExpCurve", "(x + 1) ^ 2 * 4000");

                EXP_CURVE = new Expression(RAW_EXP_CURVE.get());
                builder.pop();
            }
        }
    }
}
