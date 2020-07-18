package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class ItemSpellCaster extends Item
{
    public ItemSpellCaster(Properties properties)
    {
        super(properties);
    }

    @Override @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        LazyOptional<IManaBar> optionalBar = entityLiving.getCapability(CapabilityMP.MANA_BAR_CAP);
        LazyOptional<Spell> optionalSpell = stack.getCapability(CapabilitySpell.SPELL_CAP);
        optionalBar.ifPresent( bar ->
            optionalSpell.ifPresent(spell ->
                spell.playerCastOnRelease(bar, stack, worldIn, entityLiving, timeLeft)
            )
        );
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count)
    {
        player.getCapability(CapabilityMP.MANA_BAR_CAP).ifPresent( bar ->
            stack.getCapability(CapabilitySpell.SPELL_CAP).ifPresent(spell ->
                spell.playerCastOnHold(bar, player.world, player, stack, count)
            )
        );
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return InnerNumberDefaults.MAX_SPELL_USE_TIME;
    }

    @Override @Nonnull
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    public static Spell getSpell(PlayerEntity player, int slot)
    {
        return new SpellBuilder().build();
    }

    public void setSpellSlot(ItemStack stack, int slot)
    {
        if (slot >= 9)
        {
            throw new RuntimeException("Slot " + slot + " is not in valid range [0,9)");
        }
        if (stack.getTag() == null)
        {
            stack.setTag(new CompoundNBT());
        }
        stack.getTag().putByte("spell_slot", (byte)slot);
    }

    public int getSpellSlot(ItemStack stack)
    {
        if (stack.hasTag())
        {
            byte slot = stack.getTag().getByte("spell_slot");
            if (slot >= 0 && slot < 9)
            {
                return slot;
            }
        }
        else
        {
            stack.setTag(new CompoundNBT());
        }
        return 0;
    }
}
