package com.ustctuixue.arcaneart.api.network;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.gui.hud.manabar.ManaBar;
import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

@Data
public class PacketSyncMP
{
    public static SimpleChannel CHANNEL;

    private static int ID = 0;
    public static int nextID() {
        return ID++;
    }

    public static void registerChannel()
    {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                ArcaneArt.getResourceLocation("sync_mp"),
                () -> "1.0",
                (s) -> true,
                (s) -> true
        );
        CHANNEL.registerMessage(
                nextID(),
                PacketSyncMP.class,
                PacketSyncMP::toBytes,
                PacketSyncMP::new,
                PacketSyncMP::handle
        );
    }

    CompoundNBT manaBar;

    public PacketSyncMP(PacketBuffer buffer)
    {
        manaBar = buffer.readCompoundTag();
    }

    public PacketSyncMP(PlayerEntity playerEntity)
    {
        this.manaBar = (CompoundNBT) new CapabilityMP.Storage().writeNBT( CapabilityMP.MANA_BAR_CAP,
                playerEntity.getCapability(CapabilityMP.MANA_BAR_CAP).orElseGet(DefaultManaBar::new), null);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            LazyOptional<IManaBar> manabar = Minecraft.getInstance().player.getCapability(CapabilityMP.MANA_BAR_CAP);
            manabar.ifPresent(bar -> new CapabilityMP.Storage().readNBT(CapabilityMP.MANA_BAR_CAP, bar, null, this.manaBar));
            ctx.get().setPacketHandled(true);
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeCompoundTag(this.manaBar);
    }
}
