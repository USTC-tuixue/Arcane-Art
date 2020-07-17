package com.ustctuixue.arcaneart.api;

import net.minecraft.util.ResourceLocation;
// import net.minecraftforge.fml.common.Mod;
//
// @Mod(ArcaneArtAPI.MOD_ID)
public class ArcaneArtAPI
{
    public static final String MOD_ID = "arcane_api";
    public static final String MOD_NAME = "Arcane API";
    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
