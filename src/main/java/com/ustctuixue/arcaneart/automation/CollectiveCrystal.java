package com.ustctuixue.arcaneart.automation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CollectiveCrystal extends Block{
    public CollectiveCrystal(){
        super(Properties.create(Material.ROCK).hardnessAndResistance(5));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CollectiveCrystalTileEntity();
    }

    /*
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            CollectiveCrystalTileEntity collectiveCrystalTileEntity = (CollectiveCrystalTileEntity) worldIn.getTileEntity(pos);
            int counter = collectiveCrystalTileEntity.increase();
            TranslationTextComponent translationTextComponent = new TranslationTextComponent("message.arcaneart.counter", counter);
            player.sendStatusMessage(translationTextComponent, false);
        }
        return ActionResultType.SUCCESS;
    }
    */


}

