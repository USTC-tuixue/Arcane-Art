package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.ustctuixue.arcaneart.api.spell.SpellCasterTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class PlayerInterpreter extends Interpreter<PlayerEntity>
{
    @Override
    protected SpellCasterSource getCasterSource(PlayerEntity playerEntity)
    {
        if (playerEntity.world.isRemote)
            return null;
        return new SpellCasterSource((ServerWorld) playerEntity.world, playerEntity, null, (int)playerEntity.getAttribute(SpellCasterTiers.CASTER_TIER).getValue());
    }
}
