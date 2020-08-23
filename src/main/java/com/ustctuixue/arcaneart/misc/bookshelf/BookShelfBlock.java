package com.ustctuixue.arcaneart.misc.bookshelf;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BookShelfBlock extends Block {
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty[] BOOKS = new BooleanProperty[10];

	static {
        for (int i = 0; i < 9; i++)
        {
            BOOKS[i] = BooleanProperty.create("book" + (i + 1));
        }
        BOOKS[9] = BooleanProperty.create("bookx");
    }

    public BookShelfBlock() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(1.5f).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.SOUTH));
        for (int i = 0; i < 10; i++)
        {
            this.setDefaultState(this.getDefaultState().with(BOOKS[i], false));
        }
    }
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        builder.add(BOOKS);
        super.fillStateContainer(builder);
    }
    private static VoxelShape shape1=Block.makeCuboidShape(5, 0, 0, 11, 16, 16);
    private static VoxelShape shape2=Block.makeCuboidShape(0, 0, 5, 16, 16, 11);

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    	Direction v=state.get(HORIZONTAL_FACING);
    	if(v==Direction.SOUTH||v==Direction.NORTH)
    		return shape2;
    	return shape1;
    }
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BookShelfTileEntity();
    }

    @Nonnull
    @Override
    public PushReaction getPushReaction(BlockState state) {
       return PushReaction.BLOCK;
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            BookShelfTileEntity BookShelfTileEntity = (BookShelfTileEntity) worldIn.getTileEntity(pos);
            assert BookShelfTileEntity != null;
            InventoryHelper.dropInventoryItems(worldIn, pos, BookShelfTileEntity.getInventory());
            worldIn.removeTileEntity(pos);
        }
     }
    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            BookShelfTileEntity BookShelfTileEntity = (BookShelfTileEntity) worldIn.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, BookShelfTileEntity, (PacketBuffer packerBuffer) -> {
                assert BookShelfTileEntity != null;
                packerBuffer.writeBlockPos(BookShelfTileEntity.getPos());
            });
        }
        return ActionResultType.SUCCESS;
    }
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
     }
}
