package com.ustctuixue.arcaneart.networking;

import com.ustctuixue.arcaneart.ArcaneArt;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class KeyEvent {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;
    public static int nextID() {
        return ID++;
    }
    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                ArcaneArt.getResourceLocation("key_event"),
                () -> "1.0",
                (s) -> true,
                (s) -> true
        );
        INSTANCE.registerMessage(
                nextID(),
                KeyPack.class,
                KeyPack::toBytes,
                KeyPack::new,
                KeyPack::handler
        );
    }
}
