package com.ustctuixue.arcaneart.automation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnvHelper {
    private EnvHelper envHelper = new EnvHelper();

    public static double getTemperature(World world, BlockPos pos){
        //return the Temperature of the given world and pos
        double temperatureRaw = world.getBiome(pos).getTemperature(pos);
        double temperatureFinal = 270.0D + temperatureRaw * 20.0D + (64 - pos.getY()) / 30.0D;//biome temperature minus height factor
        return temperatureFinal;
    }
}
