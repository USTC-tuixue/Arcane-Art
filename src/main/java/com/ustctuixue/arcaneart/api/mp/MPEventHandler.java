package com.ustctuixue.arcaneart.api.mp;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MPEventHandler
{
    /**
     * 玩家死亡时保留 MP 数据
     * @param event
     */
    @SubscribeEvent
    public void persistOnDeath(PlayerEvent.Clone event)
    {
        if (event.isWasDeath())
        {
            event.getOriginal().getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((iManaBar ->
                    event.getPlayer().getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((iManaBarNew -> {
                        iManaBarNew.setMana(event.getPlayer().getAttribute(CapabilityMP.MAX_MANA).getValue()
                                * 0.25);    // TODO: 使用配置文件值
                        iManaBarNew.setRegenCoolDown(0);  // TODO：使用配置文件
                        iManaBarNew.setMagicLevel(iManaBar.getMagicLevel());
                        iManaBarNew.setMagicExperience(iManaBar.getMagicExperience());
                    }))
            ));
        }
    }

    /**
     * 玩家受到伤害时重置回复冷却计时器
     * @param event
     */
    @SubscribeEvent
    public void resetRegenTimer(LivingDamageEvent event)
    {
        event.getEntityLiving().getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((iManaBar -> {
            iManaBar.setRegenCoolDown(0);   // TODO: 使用配置文件
        }));
    }

    /**
     * 回复 MP
     * @param event
     */
    @SubscribeEvent
    public void regenMP(TickEvent.PlayerTickEvent event)
    {
        event.player.getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent((manaBar)->{
            if (manaBar.coolDown())
            {
                double regen = event.player.getAttribute(CapabilityMP.REGEN_RATE).getValue();
                double maxMP = event.player.getAttribute(CapabilityMP.MAX_MANA).getValue();
                regen = MathHelper.clamp(manaBar.getMana() + regen * maxMP, 0, maxMP);
                manaBar.setMana(regen);
                // LOGGER.info("Mana of " + event.player.getUniqueID().toString() + ":" + manaBar.getMana());
            }
        });
    }
}
