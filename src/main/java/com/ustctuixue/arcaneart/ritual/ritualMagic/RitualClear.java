package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.api.ritual.IRitualEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class RitualClear implements IRitualEffect {

    @Override
    public boolean validateOther(World world, BlockPos pos) {
        return world.canBlockSeeSky(pos.offset(Direction.UP));
    }

    @Override
    public void execute(World world, BlockPos pos, LazyOptional<PlayerEntity> caster) {
        if(!world.isRemote()) {
            world.getWorldInfo().setClearWeatherTime(6000);
            world.getWorldInfo().setRainTime(0);
            world.getWorldInfo().setThunderTime(0);
            world.getWorldInfo().setRaining(false);
            world.getWorldInfo().setThundering(false);
        }
    }

}
