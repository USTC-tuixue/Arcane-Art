package com.ustctuixue.arcaneart.automation.crystal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;


public class VoidCrystal extends Block {
    public VoidCrystal(){
        super(Properties.create(Material.ROCK).hardnessAndResistance(5).notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new VoidCrystalTileEntity();
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
       return 1.0F;
    }

}
