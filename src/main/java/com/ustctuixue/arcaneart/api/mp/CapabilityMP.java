package com.ustctuixue.arcaneart.api.mp;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class CapabilityMP
{
    public static IAttribute MAGIC_LEVEL =
            new RangedAttribute(null, "arcaneart.magicLevel",
                    1.0D, 0.0D, 10.0D)
                    .setShouldWatch(true);

    public static IAttribute MAX_MANA_BASE = new RangedAttribute(null, "arcaneart.maxManaBase",
            100.0D, 0.0D, 1000.0D);



    @SubscribeEvent
    public static void attachAttribute(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity)
        {
            ((LivingEntity) entity).getAttributes().registerAttribute(MAGIC_LEVEL);
            ((LivingEntity) entity).getAttributes().registerAttribute(MAX_MANA_BASE);
        }
    }
}
