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

        static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Crystal");
            SOLAR_CRYSTAL_REGEN_RATIO = builder
                    .comment("A working solar crystal will generate MP at this speed",
                            "Use x to refer to the biome temperature.",
                            "Result is the MP generate by the crystal per second.")
                    .define("SolarCrystalRegenRatio", "(x + 1) ^ 2 * 4000");
            builder.pop();
        }

    }
}
