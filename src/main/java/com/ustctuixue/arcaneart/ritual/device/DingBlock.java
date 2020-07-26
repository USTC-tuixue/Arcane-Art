package com.ustctuixue.arcaneart.ritual.device;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class DingBlock extends Block {
    public static enum EnumShape implements IStringSerializable {
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
    public static final BooleanProperty LOCK = BooleanProperty.create("lock");
    public static final EnumProperty<EnumShape> SHAPE = EnumProperty.create("shape", EnumShape.class);

    public DingBlock(EnumShape shape) {
        super(Properties
                .create(Material.IRON)
                .hardnessAndResistance(5)
                .notSolid());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(LOCK, false)
                .with(SHAPE, shape));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LOCK).add(SHAPE);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        switch(state.get(SHAPE)) {
            case CENTER:
                return new DingTileEntity.DingCenterTileEntity();
            case SQUARE:
                return new DingTileEntity.DingSquareTileEntity();
            default:
                return new DingTileEntity.DingCircleTileEntity();
        }
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
}
