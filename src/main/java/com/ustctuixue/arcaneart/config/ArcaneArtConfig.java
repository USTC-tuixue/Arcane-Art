package com.ustctuixue.arcaneart.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ArcaneArtConfig
{
    private static final ForgeConfigSpec COMMON_CONFIG =
            new ForgeConfigSpec.Builder().configure(CommonConfig::new).getRight();

    private static final ForgeConfigSpec CLIENT_CONFIG =
            new ForgeConfigSpec.Builder().configure(ClientConfig::new).getRight();

    private static final ForgeConfigSpec SERVER_CONFIG =
            new ForgeConfigSpec.Builder().configure(ServerConfig::new).getRight();

    public static void registerConfigs()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }
}
