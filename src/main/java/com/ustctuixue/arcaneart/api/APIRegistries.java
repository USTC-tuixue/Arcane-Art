package com.ustctuixue.arcaneart.api;

import com.ustctuixue.arcaneart.api.spell.EntitySpellBall;
import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class APIRegistries
{
    public static class Entities
    {
        public static EntityType<EntitySpellBall> SPELL_BALL_TYPE
                = EntityType.Builder
                .<EntitySpellBall>create(
                        EntitySpellBall::new, EntityClassification.MISC
                )
                .size(1.0f, 1.0f)
                .build("spell_ball");   // 这里的 ID 是给 DataFixer 用的，不影响注册键值
                                            // 众所周知，DataFixer 只是升级远古(MC v1.7)存档用的玩意儿
    }


    public static class Items
    {
        public static ItemSpell ITEM_SPELL
                = new ItemSpell(
                        new Item.Properties()
                                .maxStackSize(1)
                                .setNoRepair()
        );
    }
}
