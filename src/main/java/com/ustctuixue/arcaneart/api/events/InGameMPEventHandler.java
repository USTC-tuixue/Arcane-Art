package com.ustctuixue.arcaneart.api.events;

import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.MPEvent;
import com.ustctuixue.arcaneart.api.network.PacketSyncMP;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class InGameMPEventHandler
{
    /**
     * 玩家死亡时保留 MP 数据
     * @param event clone event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void persistOnDeath(PlayerEvent.Clone event)
    {
        if (event.isWasDeath())
        {
            event.getOriginal().getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((iManaBar ->
                    event.getPlayer().getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((iManaBarNew -> {
                        iManaBarNew.setMana(event.getPlayer().getAttribute(CapabilityMP.MAX_MANA).getValue()
                                * APIConfig.MP.DEATH_RESET_RATIO.get());
                        iManaBarNew.setRegenCoolDown(APIConfig.MP.Regeneration.COOLDOWN_TICK.get());
                        iManaBarNew.setMagicLevel(iManaBar.getMagicLevel());
                        iManaBarNew.setMagicExperience(iManaBar.getMagicExperience(), event.getEntityLiving());
                    }))
            ));
        }
    }

    /**
     * 玩家受到伤害时重置回复冷却计时器
     * @param event damage event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void resetRegenTimer(LivingDamageEvent event)
    {
        event.getEntityLiving().getCapability(CapabilityMP.MANA_BAR_CAP)
                .ifPresent(
                        iManaBar ->
                                iManaBar.setRegenCoolDown(
                                        APIConfig.MP.Regeneration.COOLDOWN_TICK.get()
                                )
                );
    }

    /**
     * 回复 MP
     * @param event tick event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void regenMP(TickEvent.PlayerTickEvent event)
    {
        // Disable according to config
        if (!APIConfig.MP.Regeneration.ENABLE.get())
        {
            return;
        }
        event.player.getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((manaBar)->{
            if (manaBar.coolDown())
            {
                double regen = event.player.getAttribute(CapabilityMP.REGEN_RATE).getValue();
                double maxMP = event.player.getAttribute(CapabilityMP.MAX_MANA).getValue();
                MPEvent.MPRegen regenEvent =
                        new MPEvent.MPRegen(event.player,
                                regen * maxMP * APIConfig.MP.Regeneration.REGEN_RATE.get()
                        );
                if (!MinecraftForge.EVENT_BUS.post(regenEvent))
                {
                    regen = MathHelper.clamp(manaBar.getMana() + regenEvent.mana, 0, maxMP);
                    manaBar.setMana(regen);
                }
                // LOGGER.info("Mana of " + event.player.getUniqueID().toString() + ":" + manaBar.getMana());
            }
        });
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public void addExp(MPEvent.CastSpell.Post event)
    {
        event.getManaBar().addMagicExperience(event.getComplexity(), event.getEntityLiving());
    }

    static int counter = 0;
    @SubscribeEvent @SuppressWarnings("unused")
    public void syncMP(TickEvent.PlayerTickEvent event)
    {
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, ()->()->
                {
                    if (++counter % 20 == 0)
                    {
                        PacketSyncMP.CHANNEL.send(PacketDistributor.PLAYER.with(
                                () -> (ServerPlayerEntity) event.player
                                ),
                                new PacketSyncMP(event.player)
                        );
                        counter = 0;
                    }
                }
        );
    }
}
