package com.ustctuixue.arcaneart.automation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnvironmentHelper {
    private EnvironmentHelper envHelper = new EnvironmentHelper();

    public static double getTemperature(World world, BlockPos pos){
        //return the Temperature of the given world and pos
        double temperatureRaw = world.getBiome(pos).getTemperature(pos);
        double temperatureFinal = 270 + temperatureRaw * 20 + (64 - pos.getY()) / 30.0;//biome temperature minus height factor
        return temperatureFinal;
    }
}
