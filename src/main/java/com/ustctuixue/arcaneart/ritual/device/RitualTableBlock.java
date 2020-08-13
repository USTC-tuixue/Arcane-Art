package com.ustctuixue.arcaneart.ritual.device;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;


public class RitualTableBlock extends Block implements IWaterLoggable {

    public RitualTableBlock() {
        super(Properties
                .create(Material.ROCK)
                .hardnessAndResistance(2.5F)
                .notSolid()
                .harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(LOCK, false)
                .with(FACE_NS, true)
                .with(BlockStateProperties.WATERLOGGED, false));
    }

    public static final BooleanProperty LOCK = BooleanProperty.create("lock");
    public static final BooleanProperty FACE_NS = BooleanProperty.create("face_ns");

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LOCK).add(FACE_NS).add(BlockStateProperties.WATERLOGGED);
        super.fillStateContainer(builder);
    }

    @Override
    public BlockState getStateForPlacement (BlockItemUseContext context) {
        boolean faceNS = context.getPlacementHorizontalFacing() == Direction.NORTH || context.getPlacementHorizontalFacing() == Direction.SOUTH;
        return this.getDefaultState().with(FACE_NS, faceNS);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RitualTableTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if( !worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            RitualTableTileEntity ritualTableTileEntity = (RitualTableTileEntity) worldIn.getTileEntity(pos);
            if(ritualTableTileEntity == null) {
                return ActionResultType.PASS;
            }
            ritualTableTileEntity.start(state, worldIn, pos, player);
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }



}
