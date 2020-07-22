package com.ustctuixue.arcaneart.api;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.network.PacketHandler;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.util.Module;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import net.minecraftforge.fml.common.Mod;
//
// @Mod(ArcaneArtAPI.MOD_ID)
@Mod.EventBusSubscriber
public class ArcaneArtAPI
    extends Module
{
    public static final String MOD_ID = "arcane_api";
    public static final String MOD_NAME = "Arcane API";
    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final PacketHandler packetHandler = new PacketHandler();

    @SubscribeEvent
    public void loadKeyWordMaps(FMLServerStartingEvent event)
    {
        packetHandler.initialize();
        LanguageManager.getInstance().readFromConfig();
    }

    @Override
    protected Object[] getModLoadingEventHandler()
    {
        return new Object[]{
                new APIEventHandler()
        };
    }

    @Override
    protected Object[] getCommonEventHandler()
    {
        return new Object[]{
                new ArcaneArtAPI(),
                new APIEventHandler(),
        };
    }
}
