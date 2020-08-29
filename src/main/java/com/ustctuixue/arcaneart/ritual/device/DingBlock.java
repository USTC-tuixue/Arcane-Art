package com.ustctuixue.arcaneart.ritual.device;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class DingBlock extends Block implements IWaterLoggable {
    public enum EnumShape implements IStringSerializable {
        CENTER("center"), SQUARE("square"), CIRCLE("circle");
        private final String name;

        EnumShape(String shape) {
            this.name = shape;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
    private static VoxelShape shapeCenter, shapeCircle, shapeSquare;
    static {
        VoxelShape circle_base = Block.makeCuboidShape(1,0,2,14,13,15);
        VoxelShape circle_E = Block.makeCuboidShape(14, 13, 6, 15, 14, 11);
        VoxelShape circle_W = Block.makeCuboidShape(0, 13, 6, 1, 14, 11);
        VoxelShape circle_N = Block.makeCuboidShape(5, 13, 1, 10, 16, 2);
        VoxelShape circle_S = Block.makeCuboidShape(5, 13, 15, 10, 16, 16);
        VoxelShape circle_Empty = Block.makeCuboidShape(4,9,5,12,14,13);
        shapeCircle= VoxelShapes.or(circle_base, circle_E, circle_W, circle_N, circle_S);
        shapeCircle = VoxelShapes.combine(shapeCircle, circle_Empty, IBooleanFunction.ONLY_FIRST);
        VoxelShape center_base = Block.makeCuboidShape(1,0,1, 15, 12, 15);
        VoxelShape center_head = Block.makeCuboidShape(0, 12, 0, 16, 13, 16);
        VoxelShape center_empty = VoxelShapes.or(
                Block.makeCuboidShape(3, 6, 3, 13, 11, 13),
                Block.makeCuboidShape(2,11,2,14,12,14),
                Block.makeCuboidShape(1,12,1,15,13,15));
        shapeCenter = VoxelShapes.or(center_base, center_head);
        shapeCenter = VoxelShapes.combine(shapeCenter, center_empty, IBooleanFunction.ONLY_FIRST);
        VoxelShape square_base = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);
        VoxelShape square_edges = VoxelShapes.or(
                Block.makeCuboidShape(0, 13, 0, 16, 14, 0),
                Block.makeCuboidShape(1, 14, 6, 2, 16, 10),
                Block.makeCuboidShape(14, 14, 6, 15, 16, 10)
        );
        VoxelShape square_empty = Block.makeCuboidShape(3, 7, 3,13, 14, 13);
        shapeSquare = VoxelShapes.or(square_base, square_edges);
        shapeSquare = VoxelShapes.combine(shapeSquare, square_empty, IBooleanFunction.ONLY_FIRST);
    }
    public static final BooleanProperty LOCK = BooleanProperty.create("lock");
    public static final EnumProperty<EnumShape> SHAPE = EnumProperty.create("shape", EnumShape.class);


    public DingBlock(EnumShape shape) {
        super(Properties
                .create(Material.IRON)
                .hardnessAndResistance(5)
                .notSolid()
                .harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(LOCK, false)
                .with(SHAPE, shape)
                .with(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LOCK).add(SHAPE).add(BlockStateProperties.WATERLOGGED);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DingTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if( !worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            DingTileEntity dingTileEntity = (DingTileEntity) worldIn.getTileEntity(pos);
            ItemStack handStack = player.getHeldItem(handIn);
            if(dingTileEntity == null) {
                return ActionResultType.PASS;
            }
            IItemHandler dingStackHandler = dingTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, hit.getFace()).orElse(new ItemStackHandler());

            if(handStack.isEmpty()) {
                int slotLimit = dingStackHandler.getSlotLimit(0);
                handStack = dingStackHandler.extractItem(0, slotLimit, false);
            }
            else {
                handStack = dingStackHandler.insertItem(0, handStack, false);
            }
            player.setHeldItem(handIn, handStack);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return super.ticksRandomly(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if(state.get(LOCK) && world.getTileEntity(pos) instanceof DingTileEntity) {
            DingTileEntity dingTileEntity = (DingTileEntity) world.getTileEntity(pos);
            if(dingTileEntity == null) {
                return 0;
            }
            if(dingTileEntity.getItemStored().isEmpty()) {
                return 6;
            }
            else {
                return 12;
            }
        }
        return super.getLightValue(state, world, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return state.with(LOCK, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(SHAPE)) {
            case CENTER: return shapeCenter;
            case CIRCLE: return shapeCircle;
            case SQUARE: return shapeSquare;
            default: return VoxelShapes.fullCube();
        }
    }
}
