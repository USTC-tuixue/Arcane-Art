package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.api.ritual.IRitualEffect;
import com.ustctuixue.arcaneart.api.spell.CapabilitySpell;
import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.ritual.device.DingTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.system.CallbackI;

import java.util.concurrent.CountDownLatch;

public class RitualAppendSpell implements IRitualEffect {
    //

    @Override
    public void execute(World world, BlockPos pos, LazyOptional<PlayerEntity> caster) {
        if(!world.isRemote()) {
            PlayerEntity player;
            if(caster.isPresent()) {
                player = caster.orElseThrow(NullPointerException::new);
            }
            else {
                player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ());
            }
            DingTileEntity te = (DingTileEntity) world.getTileEntity(pos);
            if(te == null) {
                return;
            }
            LazyOptional<IItemHandler> lazyHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if( !lazyHandler.isPresent()) {
                return;
            }
            ItemStack store = te.getItemStored();
            if(player == null) {
                return;
            }
            TranslatedSpell ts = TranslatedSpell.fromWrittenBook(player.getHeldItem(Hand.MAIN_HAND));
            if(ts != null) {
                INBT inbt = new CapabilitySpell.Storage().writeNBT(CapabilitySpell.SPELL_CAP,
                        new ITranslatedSpellProvider.Impl(ts), null);
                if(inbt != null) {
                    store.write((CompoundNBT) inbt);
                }
            }
            player.setHeldItem(Hand.OFF_HAND, store);
        }
    }

}
