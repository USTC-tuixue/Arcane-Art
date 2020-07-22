package com.ustctuixue.arcaneart.automation;

import com.ustctuixue.arcaneart.api.APIConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class AutomationConfig {
    public AutomationConfig(ForgeConfigSpec.Builder builder)
    {
        builder.push("automation");
        Crystal.load(builder);
        builder.pop();
    }

    public static class Crystal{

        public static ForgeConfigSpec.ConfigValue<String> SOLAR_CRYSTAL_REGEN_RATIO;
        public static ForgeConfigSpec.ConfigValue<String> FROZEN_CRYSTAL_REGEN_RATIO;

        static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Crystal");
            SOLAR_CRYSTAL_REGEN_RATIO = builder
                    .comment("A working solar crystal will generate MP at this speed",
                            "Use T to refer to the biome temperature in Kelvin.",
                            "Result is the MP generate by the crystal per second.",
                            "Note: Vanilla Temperature T_raw is between -0.5 and 2, with ice point 0.15. Here we assume T = 270 + 20 * T_raw")
                    .define("SolarCrystalRegenRatio", "(T-273)/10");
            FROZEN_CRYSTAL_REGEN_RATIO = builder
                    .comment("A working frozen crystal will generate MP at this speed",
                            "Use T to refer to the biome temperature in Kelvin.",
                            "Result is the MP generate by the crystal per second.")
                    .define("FrozenCrystalRegenRatio", "(303-T)/10");
            builder.pop();
        }

    }
}
