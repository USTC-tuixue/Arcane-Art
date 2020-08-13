package com.ustctuixue.arcaneart.automation.luxtransport;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/*
偏振分光棱镜（游戏里去掉了偏振的属性，仅保留分拆为两束强度相同光的特性）
 */
public class LuxSplitter extends DirectionalBlock implements IWaterLoggable {
    //FACE的选取：UP意味着半透半反面从-x-z到+x+z，DOWN意味着从-x+z到+x-z。
    //xoz平面上的四个方向则取和下台阶类似的方位，如EAST（正x）从+x-y到-x+y，SOUTH（正z）从+z-y到-z+y


    public LuxSplitter(){
        super(Properties.create(Material.ROCK).hardnessAndResistance(5));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        //放置时的face选取：若playerFace面向上或者下，那么棱镜设置为从玩家的屏幕下方入射的光线，反射光出射到玩家的方位
        //若不是上下，那么反射面尽可能与玩家视线方向垂直
        Direction playerFace = context.getNearestLookingDirection();
        if(playerFace == Direction.UP){
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
        }
        else if(playerFace == Direction.DOWN){
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
        }
        else{
            int obliqueFace = MathHelper.floor(context.getPlacementYaw() / 90.0D);
            if((obliqueFace & 1) == 0){
                return this.getDefaultState().with(FACING, Direction.UP);
            }
            else{
                return this.getDefaultState().with(FACING, Direction.DOWN);
            }
            //net.minecraft.util.Direction, line 283-289
            // An angle of 0 is SOUTH, an angle of 90 would be WEST.
            // public static Direction fromAngle(double angle){
            //     return byHorizontalIndex(MathHelper.floor(angle / 90.0D + 0.5D) & 3);
            // }
        }
    }

}
