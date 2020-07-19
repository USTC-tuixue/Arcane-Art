package com.ustctuixue.arcaneart.api.mp;

import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

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
    public static class MPConsumeStart extends MPEvent
    {
        /**
         * µ¥Î»ÊÇ MP / tick
         */
        private final double rate;
        public MPConsumeStart(LivingEntity livingEntity, double rate)
        {
            super(livingEntity);
            this.rate = rate;
        }
    }

    public static class MPConsumeEnd extends MPEvent
    {
        public MPConsumeEnd (LivingEntity entity)
        {
            super(entity);
        }
    }

    @Cancelable
    public static class MPRegen extends MPEvent
    {
        public final double mana;
        public MPRegen(LivingEntity entity, double mana)
        {
            super(entity);
            this.mana = mana;
        }
    }
}
