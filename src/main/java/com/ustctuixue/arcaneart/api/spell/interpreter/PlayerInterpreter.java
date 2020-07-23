package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.ustctuixue.arcaneart.api.spell.SpellCasterTiers;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerInterpreter extends Interpreter<PlayerEntity>
{
    @Override
    protected SpellCasterSource getCasterSource(PlayerEntity playerEntity)
    {
        return new SpellCasterSource(playerEntity.world, playerEntity, null, (int)playerEntity.getAttribute(SpellCasterTiers.CASTER_TIER).getValue());
    }
}
