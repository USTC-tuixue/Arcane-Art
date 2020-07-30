package com.ustctuixue.arcaneart.ritual.device;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;


public class TableBlock extends Block implements IWaterLoggable {

    public TableBlock() {
        super(Properties
                .create(Material.ROCK)
                .hardnessAndResistance(2.5F)
                .notSolid()
                .harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(LOCK, false)
                .with(FACE_NS, true));
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


}
