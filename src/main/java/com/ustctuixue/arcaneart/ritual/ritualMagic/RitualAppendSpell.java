package com.ustctuixue.arcaneart.ritual.ritualMagic;

import com.ustctuixue.arcaneart.api.ritual.IRitualEffect;
import com.ustctuixue.arcaneart.api.spell.CapabilitySpell;
import com.ustctuixue.arcaneart.api.spell.ITranslatedSpellProvider;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.Interpreter;
import com.ustctuixue.arcaneart.ritual.device.DingTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.system.CallbackI;

import java.util.concurrent.CountDownLatch;

public abstract class RitualAppendSpell implements IRitualEffect {
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
            if(player != null) {
                TranslatedSpell ts = TranslatedSpell.fromWrittenBook(player.getHeldItem(Hand.MAIN_HAND));
                if(ts != null) {
                    DingTileEntity te = (DingTileEntity) world.getTileEntity(pos);
                    if(te != null) {
                        LazyOptional<IItemHandler> lazyHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                        if(lazyHandler.isPresent()) {
                            ItemStack store = te.getItemStored();
                            IItemHandler handler = lazyHandler.orElse(new ItemStackHandler());

                            INBT inbt = new CapabilitySpell.Storage().writeNBT(CapabilitySpell.SPELL_CAP,
                                    new ITranslatedSpellProvider.Impl(ts), null);
                            if(inbt != null) {
                                store.write((CompoundNBT) inbt);
                            }

                        }
                    }
                }
            }
        }
    }

}
