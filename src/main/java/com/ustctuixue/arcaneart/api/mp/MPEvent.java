package com.ustctuixue.arcaneart.api.mp;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class MPEvent extends LivingEvent
{
    private final double maxMP;

    public MPEvent(LivingEntity player)
    {
        super(player);
        maxMP = player.getAttribute(CapabilityMP.MAX_MANA).getValue();
    }
}
