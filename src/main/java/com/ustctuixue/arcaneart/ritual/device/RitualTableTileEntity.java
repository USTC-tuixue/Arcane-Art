package com.ustctuixue.arcaneart.ritual.device;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.mp.tile.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.tile.MPStorage;
import com.ustctuixue.arcaneart.api.ritual.Ritual;
import com.ustctuixue.arcaneart.ritual.RitualConfig;
import com.ustctuixue.arcaneart.ritual.RitualRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class RitualTableTileEntity extends TileEntity {
    public RitualTableTileEntity() {
        super(RitualRegistries.ritualTableTileEntity.get());
        mpStorage.setMaxMP(10000);
        mpStorage.setMana(0);
        neverExec = RitualConfig.HIGHEST_HEIGHT_OF_DING.get() < RitualConfig.LOWEST_HEIGHT_OF_DING.get() ||
                RitualConfig.LONGEST_DISTANCE_FROM_TABLE_TO_DING.get() < RitualConfig.SHORTEST_DISTANCE_FROM_TABLE_TO_DING.get();
    }

    protected MPStorage mpStorage = new MPStorage();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityMPStorage.MP_STORAGE_CAP) {
            return LazyOptional.of(()->mpStorage).cast();
        }
        return super.getCapability(cap);
    }

    private PlayerEntity playerEntity = null;
    private BlockPos [] dingPos = new BlockPos[9];
    private BlockPos tablePos;
    private World world;
    private BlockState state;
    private Ritual ritual;
    private int manaConsumed = 0;
    private boolean neverExec = false;


    public boolean preExecRitual(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player) {
        ArcaneArtAPI.LOGGER.info("Checking if ritual can be executed.");
        if(neverExec || player == null) {
            return false;
        }
        Direction playerFacing = player.getHorizontalFacing();
        if(blockState.get(RitualTableBlock.FACE_NS) ^ (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH)) {
            player.sendMessage(new StringTextComponent("Wrong facing!"));
            return false;
        }
        if( !findDing(player.getHorizontalFacing(), pos)) {
            player.sendMessage(new StringTextComponent("Cannot find Enough Dings!"));
            return false;
        }
        return false;
    }

    public void execRitual(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player) {

    }

    public boolean keepRitual(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player) {
        return false;
    }

    public void cancelRitual() {
        for(BlockPos bp : dingPos) {
            if(bp != null) {
                world.setBlockState(bp, state.with(DingBlock.LOCK, false));
            }
            bp = null;
        }
        world.setBlockState(tablePos, state.with(RitualTableBlock.LOCK, false));
        world = null;
        state = null;
        playerEntity = null;
    }

    public void finishRitual(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player) {

    }

    private boolean findDing(Direction facing, BlockPos pos) {
        return false;
    }
}
