package com.ustctuixue.arcaneart.ritual.device;

import com.ustctuixue.arcaneart.api.APIConfig;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.mpstorage.CapabilityMPStorage;
import com.ustctuixue.arcaneart.api.mp.mpstorage.MPStorage;
import com.ustctuixue.arcaneart.api.ritual.IRitualEffect;
import com.ustctuixue.arcaneart.api.ritual.Ritual;
import com.ustctuixue.arcaneart.ritual.RitualConfig;
import com.ustctuixue.arcaneart.ritual.RitualRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RitualTableTileEntity extends TileEntity implements ITickableTileEntity {
    public RitualTableTileEntity() {
        super(RitualRegistries.ritualTableTileEntity.get());
        mpStorage.setMaxMana(10000);
        mpStorage.setMana(6000);
        neverExec = RitualConfig.HIGHEST_HEIGHT_OF_DING.get() < RitualConfig.LOWEST_HEIGHT_OF_DING.get() ||
                RitualConfig.LONGEST_DISTANCE_FROM_TABLE_TO_DING.get() < RitualConfig.SHORTEST_DISTANCE_FROM_TABLE_TO_DING.get();
        tableCostAmplifier = RitualConfig.RITUAL_MANA_AMPLIFIER.get();
    }

    protected final MPStorage mpStorage = new MPStorage();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityMPStorage.MP_STORAGE_CAP) {
            return LazyOptional.of(()->mpStorage).cast();
        }
        return super.getCapability(cap);
    }


    private BlockPos [] dingPos = new BlockPos[9];
    private IItemHandler [] dingItemHandlers = new IItemHandler[9];
    private PlayerEntity playerEntity = null;
    private Direction playerFacing;
    private BlockPos tablePos = this.pos;
    private Ritual ritual;
    private World worldIn = this.world;
    private double manaConsumed = 0;
    private boolean neverExec = false;
    private int executeStage = -1;
    private double totalMana;
    private double consumeSpeed;
    private IRitualEffect ritualEffect;
    private double tableCostAmplifier;
    private boolean isCreativePlayer = false;


    @SuppressWarnings("WeakerAccess")
    public void start(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player) {
        if(executeStage != -1 || neverExec || player == null ) {
            return;
        }
        executeStage = 0;
        worldIn.setBlockState(pos, blockState.with(RitualTableBlock.LOCK, true));
        this.playerEntity = player;
        this.playerFacing = player.getHorizontalFacing();
        this.tablePos = pos;
        this.worldIn = worldIn;
        this.manaConsumed = 0;
        if(!worldIn.isRemote()) {
            isCreativePlayer = ((ServerPlayerEntity) player).interactionManager.getGameType() == GameType.CREATIVE;
        }
    }

    @Override
    public void tick() {
        switch (executeStage) {
            case -1: return;
            case 0:
                if(!checkRitualStructure()) {
                    cancelRitual();
                    return;
                }
                break;
            case 1:
                if(!matchRitual()) {
                    cancelRitual();
                    return;
                }
                break;
            case 12000:
                cancelRitual();
                return;
            default:
                if(!keepRitual()) {
                    cancelRitual();
                    return;
                }
        }
        executeStage++;
    }

    private boolean checkRitualStructure() {
        ArcaneArtAPI.LOGGER.info("Checking if ritual structure is complete.");
        if( !findDing(worldIn, playerEntity.getHorizontalFacing(), pos)) {
            sendMessage("msg.arcaneart.ritual.illegal_ding");
            return false;
        }
        for(BlockPos bp : dingPos) {
            worldIn.setBlockState(bp, worldIn.getBlockState(bp).with(DingBlock.LOCK, true));
        }
        return true;
    }

    private boolean matchRitual() {
        ArcaneArtAPI.LOGGER.info("Checking if there is a matching recipe.");
        ItemStack[] items = new ItemStack[9];

        for(int i = 0; i < 9; ++i) {
            items[i] = dingItemHandlers[i].getStackInSlot(0);
        }

        boolean hasFound = false;
        for(Ritual ap : Ritual.REGISTRY) {
            if(ap.matches(items)) {
                hasFound = true;
                ritual = ap;
                break;
            }
        }
        if(!hasFound) {
            sendMessage("msg.arcaneart.ritual.no_recipe");
            return false;
        }
        //*/
        String ritualName = ritual.getRegistryName() == null ?
                "arcaneart.ritual.anonymous" : ritual.getRegistryName().toString();
        this.ritualEffect = ritual.getExecRitual().get();
        if(!ritualEffect.validateRitualCondition(worldIn, pos)) {
            playerEntity.sendMessage(new TranslationTextComponent("msg.arcaneart.ritual.not_valid",
                    new TranslationTextComponent(ritualName)));
            return false;
        }
        else {
            playerEntity.sendMessage(new TranslationTextComponent("msg.arcaneart.ritual.begin",
                    new TranslationTextComponent(ritualName)));
        }
        this.totalMana = isCreativePlayer ? 0 : ritual.getCost() * APIConfig.MP.MANA_COST_AMPLIFIER.get();
        this.consumeSpeed = ritual.getConsumeSpeed();
        return true;
    }

    /**
     *
     * @return 返回仪式是否还要继续
     */
    private boolean keepRitual() {
        BlockState nowTableState = worldIn.getBlockState(tablePos);
        if(checkAllDings(worldIn, true)
                && nowTableState.get(RitualTableBlock.LOCK)
                && checkItemsInDings()
                && worldIn.getPlayers().contains(playerEntity)
                && playerEntity.getHorizontalFacing() == playerFacing
                && playerEntity.getCapability(CapabilityMP.MANA_BAR_CAP).isPresent()) {

            IManaBar playerMana = playerEntity.getCapability(CapabilityMP.MANA_BAR_CAP).orElse(new DefaultManaBar());
            double tableRealCost = Math.min(consumeSpeed*tableCostAmplifier, this.mpStorage.getMana());
            double playerRealCost = Math.min(consumeSpeed-tableRealCost, playerMana.getMana());

            if(manaConsumed + tableRealCost + playerRealCost >= totalMana) {
                playerRealCost = totalMana - manaConsumed - tableRealCost;
                manaConsumed = totalMana;
            }
            else {
                manaConsumed += tableRealCost + playerRealCost;
            }

            this.mpStorage.consumeMana(tableRealCost);
            playerMana.consumeMana(playerRealCost);

            if(manaConsumed == totalMana) {
                this.finishRitual();
                return false;
            }
            return true;
        }
        sendMessage("msg.arcaneart.ritual.cancel");
        return false;
    }

    private void finishRitual() {
        if(!isCreativePlayer) for(IItemHandler i : dingItemHandlers) {
            i.extractItem(0, 1, false);
        }
        ritualEffect.execute(world, dingPos[4], LazyOptional.of(()->playerEntity));
        sendMessage("msg.arcaneart.ritual.finish");
    }

    private void cancelRitual() {
        if(worldIn != null)
        {
            for (BlockPos bp : dingPos)
            {
                if (bp != null && worldIn.getBlockState(bp).has(DingBlock.LOCK))
                {
                    worldIn.setBlockState(bp, worldIn.getBlockState(bp).with(DingBlock.LOCK, false));
                }
            }
        }
        if(worldIn != null && tablePos != null && worldIn.getBlockState(tablePos).has(RitualTableBlock.LOCK)) {
            worldIn.setBlockState(tablePos, getBlockState().with(RitualTableBlock.LOCK, false));
        }
        executeStage = -1;
    }

    /**
     * 此函数在给定的参数下，将检查到的合法的鼎的位置存进this.dingPos[]，将对应的IItemHandler存进this.dingItemHandlers[]
     * 如果鼎的位置不合法将返回false
     * @param worldIn 所在世界
     * @param facing 仪式发动人的水平朝向
     * @param pos 玉几所在的位置
     * @return 是否找到了全部位置合理的鼎
     */
    private boolean findDing(World worldIn, Direction facing, BlockPos pos) {
        int dMin = RitualConfig.SHORTEST_DISTANCE_FROM_TABLE_TO_DING.get()-3;//为了设置好写，写的是距离中心鼎的水平距离，
        int dMax = RitualConfig.LONGEST_DISTANCE_FROM_TABLE_TO_DING.get()-2;//但检测的时候先检测边鼎会省去部分工作量
        int hMin = RitualConfig.LOWEST_HEIGHT_OF_DING.get();
        int hMax = RitualConfig.HIGHEST_HEIGHT_OF_DING.get();
        int d, h;

        BlockPos current;

        BlockPos nearEdgeDingPos = null;
        int dingInterSpace = 0;
findEdgeDing:
        for(d = dMin; d <= dMax; ++d) {
            current = pos.offset(facing, d);
            for(h = hMin; h <= hMax; ++h) {
                if(checkDing(worldIn, current.offset(Direction.UP, h), DingBlock.EnumShape.CIRCLE, false)) {
                    nearEdgeDingPos = current.offset(Direction.UP, h);
                    break findEdgeDing;
                }
            }
        }
        if(nearEdgeDingPos == null) {
            return false;
        }

        if(checkDing(worldIn, nearEdgeDingPos.offset(facing, 1), DingBlock.EnumShape.CENTER, false)) {
            dingInterSpace = 1;
        }
        else if(checkDing(worldIn, nearEdgeDingPos.offset(facing, 2), DingBlock.EnumShape.CENTER, false)) {
            dingInterSpace = 2;
        }
        else {
            return false;
        }

        calculateBlockPos(nearEdgeDingPos, facing, dingInterSpace);
        if (!checkAllDings(worldIn, false)) {
            return false;
        }
        for(int i = 0; i < 9; ++i) {
            TileEntity te = worldIn.getTileEntity(dingPos[i]);
            if(te == null) {
                return false;
            }
            dingItemHandlers[i] = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(new ItemStackHandler());
        }
        return true;
    }

    /**
     * 本函数用于检查所有鼎是否在位
     * @param worldIn 当前世界
     * @param isLock 鼎是否上锁，在pre阶段检查到的鼎应该无锁，在keep阶段检查到的鼎应该有锁
     * @return 所有的鼎都正常返回true
     */
    private boolean checkAllDings(World worldIn, Boolean isLock) {
        final DingBlock.EnumShape [] dingShape = {
                DingBlock.EnumShape.SQUARE, DingBlock.EnumShape.CIRCLE, DingBlock.EnumShape.SQUARE,
                DingBlock.EnumShape.CIRCLE, DingBlock.EnumShape.CENTER, DingBlock.EnumShape.CIRCLE,
                DingBlock.EnumShape.SQUARE, DingBlock.EnumShape.CIRCLE, DingBlock.EnumShape.SQUARE,
        };
        for(int i = 0; i < 9; ++i) {
            if(!checkDing(worldIn, dingPos[i], dingShape[i], isLock)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDing(World worldIn, BlockPos pos, DingBlock.EnumShape shape, Boolean isLock) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof DingTileEntity) {
            BlockState state = worldIn.getBlockState(pos);
            return state.get(DingBlock.SHAPE) == shape && state.get(DingBlock.LOCK) == isLock;
        }
        return false;
    }
    private void calculateBlockPos(BlockPos nearer, Direction face, int interSpace) {
        dingPos[7] = nearer;
        dingPos[6] = dingPos[7].offset(face.rotateYCCW(), interSpace);
        dingPos[8] = dingPos[7].offset(face.rotateY(), interSpace);

        dingPos[4] = nearer.offset(face, interSpace);
        dingPos[3] = dingPos[4].offset(face.rotateYCCW(), interSpace);
        dingPos[5] = dingPos[4].offset(face.rotateY(), interSpace);

        dingPos[1] = dingPos[4].offset(face, interSpace);
        dingPos[0] = dingPos[1].offset(face.rotateYCCW(), interSpace);
        dingPos[2] = dingPos[1].offset(face.rotateY(), interSpace);
    }

    private boolean checkItemsInDings() {
        ItemStack[] items = new ItemStack[9];

        for(int i = 0; i < 9; ++i) {
            items[i] = dingItemHandlers[i].getStackInSlot(0);
        }
        return this.ritual.matches(items);
    }

    @Override
    protected void finalize() throws Throwable {
        if(this.worldIn != null && !this.worldIn.isRemote) {
            cancelRitual();
        }
        super.finalize();
    }

    @Override
    public void remove() {
        if(this.worldIn != null && !this.worldIn.isRemote) {
            cancelRitual();
        }
        super.remove();
    }

    private void sendMessage(String key) {
        if(playerEntity != null) {
            playerEntity.sendMessage(new TranslationTextComponent(key));
        }
    }

    @Override
    public void onChunkUnloaded() {
        cancelRitual();
    }

    @Override
    public void read(CompoundNBT compound) {
        mpStorage.deserializeNBT(compound.getCompound("mana"));
        super.read(compound);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("mana", mpStorage.serializeNBT());
        return super.write(compound);
    }
}
