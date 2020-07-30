package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.api.ritual.IRitual;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class RitualRain implements IRitual {

    @Override
    public boolean validateOther(World world, BlockPos pos) {
        return world.canSeeSky(pos);
    }

    @Override
    public void execute(World world, BlockPos pos, LazyOptional<PlayerEntity> caster) {
        if(!world.isRemote()) {
            world.setRainStrength(world.getRainStrength(1F)+10F);
        }
    }
}
