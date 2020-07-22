package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
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
import javax.annotation.Nullable;
import java.util.List;

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
        if (!(entityLiving instanceof PlayerEntity))
            return;
        if (!worldIn.isRemote)
        {
            SpellCasterSource source = new SpellCasterSource(worldIn, entityLiving, null);
            TranslatedSpell spell = getSpell((PlayerEntity) entityLiving, getSpellSlot(stack));
            List<String> commands = Lists.newArrayList();
            commands.addAll(spell.getCommonSentences());
            commands.addAll(spell.getOnReleaseSentences());
            commands.forEach(c -> SpellDispatcher.executeSpell(c, source));
        }

    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entityLiving, int count)
    {
        if (!(entityLiving instanceof PlayerEntity))
            return;
        World worldIn = entityLiving.getEntityWorld();
        if (!worldIn.isRemote())
        {
            SpellCasterSource source = new SpellCasterSource(worldIn, entityLiving, null);
            TranslatedSpell spell = getSpell((PlayerEntity) entityLiving, getSpellSlot(stack));
            List<String> commands = Lists.newArrayList();
            commands.addAll(spell.getCommonSentences());
            commands.addAll(spell.getOnHoldSentences());
            commands.forEach(c -> SpellDispatcher.executeSpell(c, source));
        }
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

    @Nonnull
    private static TranslatedSpell getSpell(PlayerEntity player, int slot)
    {
        ISpellInventory inventory = player.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY).orElse(new SpellInventory());
        ItemStack itemSpellStack = inventory.getShortcut(slot);
        if (itemSpellStack.getItem() instanceof ItemSpell)
            return ((ItemSpell) itemSpellStack.getItem()).getSpell(itemSpellStack);
        return new TranslatedSpell();
    }

    public void setSpellSlot(ItemStack stack, int slot)
    {
        if (slot >= 9 || slot < 0)
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
        if (stack.getTag() != null)
        {
            byte slot = stack.getTag().getByte("spell_slot");
            if (slot >= 0 && slot < 9)
            {
                return slot;
            }
        }
        this.setSpellSlot(stack, 0);    // If not a valid spell slot id, set to 0
        return 0;
    }
}
