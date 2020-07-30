package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import net.minecraftforge.common.ForgeConfigSpec;

public class AutomationConfig {
    public AutomationConfig(ForgeConfigSpec.Builder builder)
    {
        builder.push("automation");
        Crystal.load(builder);
        builder.pop();
    }

    public static class Crystal{

        public static ForgeConfigSpec.DoubleValue CRYSTAL_MAX_MP;
        public static ForgeConfigSpec.DoubleValue CRYSTAL_MAX_OUTPUT;
        public static ForgeConfigSpec.ConfigValue<String> SOLAR_CRYSTAL_REGEN_RATIO;
        public static ForgeConfigSpec.ConfigValue<String> FROZEN_CRYSTAL_REGEN_RATIO;
        public static ForgeConfigSpec.DoubleValue MAGMA_CRYSTAL_REGEN_RATIO;

        static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Crystal");

            CRYSTAL_MAX_MP = builder
                    .comment("Max MP capacity of a crystal")
                    .defineInRange("MaxCrystalMP", 1000.0D, 0.0D, 100000.0D);

            /*
            CRYSTAL_MAX_OUTPUT = builder
                    .comment("Max output speed of a crystal")
                    .defineInRange("MaxCrystalOutput", 50.0D, 0.0D, 100000.0D);
             */

            SOLAR_CRYSTAL_REGEN_RATIO = builder
                    .comment("MP generated by a working solar crystal per second",
                            "Use T to refer to the temperature in Kelvin.",
                            "Note: Vanilla Temperature T_raw is between -0.5 and 2, with ice point 0.15. Here we assume T = 270 + 20 * T_raw")
                    .define("SolarCrystalRegenRatio", "(T-273)/10");

            FROZEN_CRYSTAL_REGEN_RATIO = builder
                    .comment("MP generated by a working frozen crystal per second",
                            "Use T to refer to the temperature in Kelvin.")
                    .define("FrozenCrystalRegenRatio", "(303-T)/10");

            MAGMA_CRYSTAL_REGEN_RATIO = builder
                    .comment("MP generated by a working magma crystal per second")
                    .defineInRange("MagmaCrystalRegenRatio", 2.0D, 0.0D, InnerNumberDefaults.MAX_ALLOWED_MP);

            builder.pop();
        }

    }
}
