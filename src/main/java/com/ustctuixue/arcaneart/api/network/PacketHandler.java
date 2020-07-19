package com.ustctuixue.arcaneart.api.network;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler
{
    private int index = 0;

    protected static SimpleChannel createChannel(ResourceLocation name) {
        return NetworkRegistry.ChannelBuilder.named(name)
                .clientAcceptedVersions(getProtocolVersion()::equals)
                .serverAcceptedVersions(getProtocolVersion()::equals)
                .networkProtocolVersion(PacketHandler::getProtocolVersion)
                .simpleChannel();
    }

    private static String getProtocolVersion()
    {
        return "0.0.1";
    }

    protected <MSG> void registerClientToServer(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder,
                                                BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    protected <MSG> void registerServerToClient(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder,
                                                BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        getChannel().registerMessage(index++, type, encoder, decoder, consumer, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public void initialize()
    {
        registerClientToServer(PacketSwitchSpell.class, PacketSwitchSpell::encode, PacketSwitchSpell::decode, PacketSwitchSpell::handle);
    }

    private static SimpleChannel NET_HANDLER = createChannel(ArcaneArtAPI.getResourceLocation("switch_spell"));

    public SimpleChannel getChannel()
    {
        return NET_HANDLER;
    }
}
