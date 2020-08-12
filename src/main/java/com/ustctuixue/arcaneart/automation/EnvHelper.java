package com.ustctuixue.arcaneart.automation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnvHelper {
    private EnvHelper envHelper = new EnvHelper();

    public static double getTemperature(World world, BlockPos pos){
        //return the Temperature of the given world and pos
        //below 0K means snow instead of rain.
        double temperatureRaw = world.getBiome(pos).getTemperature(pos);
        return 270.0D + (temperatureRaw + (64 - pos.getY()) * 0.0016) * 20.0D;
    }

}
