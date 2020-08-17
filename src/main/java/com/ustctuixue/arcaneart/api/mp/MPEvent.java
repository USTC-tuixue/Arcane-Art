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
        @Getter
        private final double manaCost;
        @Getter
        private final double complexity;

        CastSpell(LivingEntity livingEntity, boolean isPersistent, double manaCostIn, double complexityIn)
        {
            super(livingEntity);
            this.isPersistent = isPersistent;
            this.manaCost = manaCostIn;
            this.complexity = complexityIn;
        }

        @Cancelable
        public static class Pre extends CastSpell
        {
            public Pre(LivingEntity livingEntity, boolean isPersistent, double manaCostIn, double complexityIn)
            {
                super(livingEntity, isPersistent, manaCostIn, complexityIn);
            }
        }

        public static class Post extends CastSpell
        {
            public Post(LivingEntity livingEntity, boolean isPersistent, double manaCostIn, double complexityIn)
            {
                super(livingEntity, isPersistent, manaCostIn, complexityIn);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class LevelUp extends MPEvent
    {
        @Getter
        private final int oldLevel;

        @Getter
        private final int newLevel;

        public LevelUp(LivingEntity livingEntity, int oldLevelIn, int newLevelIn)
        {
            super(livingEntity);
            this.newLevel = newLevelIn;
            this.oldLevel = oldLevelIn;
        }
    }
}
