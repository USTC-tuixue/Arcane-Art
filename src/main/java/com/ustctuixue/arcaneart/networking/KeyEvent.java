package com.ustctuixue.arcaneart.networking;

import net.minecraft.util.ResourceLocation;
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
                new ResourceLocation("arcaneart" + ":key_event"),
                () -> "1.0",
                (s) -> true,
                (s) -> true
        );
        INSTANCE.registerMessage(
                nextID(),
                KeyPack.class,
                (pack, buffer) -> {
                    pack.toBytes(buffer);
                },
                (buffer) -> {
                    return new KeyPack(buffer);
                },
                (pack,ctx) ->{
                    pack.handler(ctx);
                }
        );
    }
}
