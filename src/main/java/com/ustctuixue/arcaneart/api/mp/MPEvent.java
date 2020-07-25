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
    public static class MPRegen extends MPEvent
    {
        public final double mana;
        public MPRegen(LivingEntity entity, double mana)
        {
            super(entity);
            this.mana = mana;
        }
    }

    public static class CastSpell extends MPEvent
    {
        @Getter
        private final boolean isPersistent;
        public CastSpell(LivingEntity livingEntity, boolean isPersistent)
        {
            super(livingEntity);
            this.isPersistent = isPersistent;
        }

        @Cancelable
        public static class Pre extends CastSpell
        {
            public Pre(LivingEntity livingEntity, boolean isPersistent)
            {
                super(livingEntity, isPersistent);
            }
        }

        public static class Post extends CastSpell
        {
            public Post(LivingEntity livingEntity, boolean isPersistent)
            {
                super(livingEntity, isPersistent);
            }
        }
    }

}
