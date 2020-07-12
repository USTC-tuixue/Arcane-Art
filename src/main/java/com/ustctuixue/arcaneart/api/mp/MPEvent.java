package com.ustctuixue.arcaneart.api.mp;

import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class MPEvent extends LivingEvent
{
    @Getter
    private final double maxMp;
    @Getter
    private final IManaBar manaBar;

    public MPEvent(LivingEntity livingEntity)
    {
        super(livingEntity);
        maxMp = livingEntity.getAttribute(CapabilityMP.MAX_MANA).getValue();
        manaBar = livingEntity.getCapability(CapabilityMP.MANA_BAR_CAP).orElseGet(
                DefaultManaBar::new
        );
    }

    @Cancelable
    public static class MPConsumeEvent extends MPEvent
    {
        @Getter
        private final double consume;
        public MPConsumeEvent(LivingEntity player, double consume)
        {
            super(player);
            this.consume = consume;
        }

    }
}
