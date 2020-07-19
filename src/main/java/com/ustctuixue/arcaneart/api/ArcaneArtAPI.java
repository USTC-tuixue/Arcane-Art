package com.ustctuixue.arcaneart.api;

import com.ustctuixue.arcaneart.api.network.PacketHandler;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

// import net.minecraftforge.fml.common.Mod;
//
// @Mod(ArcaneArtAPI.MOD_ID)
@Mod.EventBusSubscriber
public class ArcaneArtAPI
{
    public static final String MOD_ID = "arcane_api";
    public static final String MOD_NAME = "Arcane API";
    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final PacketHandler packetHandler = new PacketHandler();

    @SubscribeEvent
    public void loadKeyWordMaps(FMLServerAboutToStartEvent event)
    {
        packetHandler.initialize();
        LanguageManager.getInstance().readFromConfig();
    }

}
