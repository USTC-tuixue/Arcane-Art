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

    public static class CastPersistentSpell extends MPEvent
    {
        public CastPersistentSpell(LivingEntity livingEntity)
        {
            super(livingEntity);
        }

        @Cancelable
        public static class Pre extends CastPersistentSpell
        {
            public Pre(LivingEntity livingEntity)
            {
                super(livingEntity);
            }
        }

        public static class Post extends CastPersistentSpell
        {
            public Post(LivingEntity livingEntity)
            {
                super(livingEntity);
            }
        }
    }

    public static class CastInstantSpell extends MPEvent
    {
        public CastInstantSpell(LivingEntity livingEntity)
        {
            super(livingEntity);
        }

        @Cancelable
        public static class Pre extends CastInstantSpell
        {
            public Pre(LivingEntity livingEntity)
            {
                super(livingEntity);
            }
        }

        public static class Post extends CastInstantSpell
        {
            public Post(LivingEntity livingEntity)
            {
                super(livingEntity);
            }
        }
    }


}
