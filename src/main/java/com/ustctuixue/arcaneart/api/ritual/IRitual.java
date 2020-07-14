package com.ustctuixue.arcaneart.api.ritual;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.LazyOptional;

public interface IRitual
{
    default boolean validateTimeOfDay(long timeOfDay)
    {
        return true;
    }

    default boolean validateBiome(Biome biome)
    {
        return true;
    }

    default boolean validateOther(World world, BlockPos pos)
    {
        return true;
    }

    default boolean validateRitualCondition(World world, BlockPos pos)
    {
        boolean flagTime = validateTimeOfDay(world.getDayTime());
        boolean flagBiome = validateBiome(world.getBiome(pos));
        boolean flagOther = validateOther(world, pos);
        return flagTime && flagBiome && flagOther;
    }

    /**
     * 施法效果
     * @param world 仪式核心祭坛的位置
     * @param pos 仪式核心祭坛的位置
     * @param caster 可选的施法人
     */
    void execute(World world, BlockPos pos, LazyOptional<PlayerEntity> caster);

    /**
     * 检查并消耗牺牲
     * @param world 仪式核心祭坛的位置
     * @param pos 仪式核心祭坛的位置
     * @param simulate 是否模拟，true 时不应消耗牺牲
     * @return 消耗是否成功
     */
    boolean consumeSacrifice(World world, BlockPos pos, boolean simulate);
}
