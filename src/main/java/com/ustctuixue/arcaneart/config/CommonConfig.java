package com.ustctuixue.arcaneart.config;

import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.automation.AutomationConfig;
import com.ustctuixue.arcaneart.spell.SpellModuleConfig;
import net.minecraftforge.common.ForgeConfigSpec;

class CommonConfig
{
    CommonConfig(ForgeConfigSpec.Builder builder)
    {
        new APIConfig(builder);
        new AutomationConfig(builder);
        new SpellModuleConfig(builder);
    }
}
