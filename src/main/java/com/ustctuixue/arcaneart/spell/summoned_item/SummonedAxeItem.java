package com.ustctuixue.arcaneart.spell.summoned_item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SummonedAxeItem extends AxeItem
{
    public SummonedAxeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new SpellCasterHolderCapabilityProvider();
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            LazyOptional<ItemStackHandler> handler
                    = stack.getCapability(SpellCasterHolderCapabilityProvider.SPELL_CASTER_HOLDER_CAP).cast();
            if (!isSelected && entityIn instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entityIn;
                handler.ifPresent(stored -> player.inventory.setInventorySlotContents(itemSlot, stored.getStackInSlot(0)));
            }
            else if (!handler.isPresent())
            {
                stack.shrink(stack.getCount());
            }
        }
    }
}
