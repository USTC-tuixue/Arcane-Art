package com.ustctuixue.arcaneart.automation.luxtransport;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/*
直角棱镜/反射棱镜
 */
public class LuxReflector extends Block implements IWaterLoggable {
    //FACE的选取：UP意味着反射面从-x+z到+x-z，DOWN意味着从-x-z到+x+z。
    //xoz平面上的四个方向则取和下台阶类似的方位，如EAST（正x）从+x-y到-x+y，SOUTH（正z）从+z-y到-z+y

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public int redstonePower = 0;

    public LuxReflector(){
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3f).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH));
    }

    private static VoxelShape shape=Block.makeCuboidShape(2, 2, 2, 14, 14, 14);

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    //*红石信号的part

    //是否可以产生红石信号
    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    //返回弱红石信号
    // http://cncc.bingj.com/cache.aspx?q=getWeakPower&d=4745015479046279&mkt=zh-CN&setlang=zh-CN&w=mmjzMmiuaT1tyOllGwSCPteTPKrIxbyE
    // https://minecraft.gamepedia.com/Mechanics/Redstone/Circuit#Power
    // （不可以充能其他红石粉？为什么？
    // 应该可以添加新的blockstate来做，由法球触发方块更新
    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side){
        return redstonePower;
    }

     //*/

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace();
        BlockPos blockpos = context.getPos();
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}

