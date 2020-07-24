package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.ParseResults;
import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import com.ustctuixue.arcaneart.api.mp.MPEvent;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.inventory.ISpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventory;
import com.ustctuixue.arcaneart.api.spell.inventory.SpellInventoryCapability;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class ItemSpellCaster extends Item
{
    @Getter
    private final int tier;
    public ItemSpellCaster(Properties properties, int tierIn)
    {
        super(properties);
        this.tier = tierIn;
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
        // Fire pre instant spell event, will not be executed if cancelled
        if (!worldIn.isRemote && MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Pre(entityLiving, true)))
        {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            SpellCasterSource source = new SpellCasterSource(serverWorld, entityLiving, null, tier);
            ITranslatedSpellProvider spellProvider = getSpellProvider((PlayerEntity) entityLiving, getSpellSlot(stack));
            // Execute common spell sentences first
            spellProvider.preCompile(source);
            SpellDispatcher.executeSpell(spellProvider.getSpell().getOnReleaseSentences(), source);

            // Fire post instant spell event
            MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Post(entityLiving, true));
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND)
        {
            multimap.put(SpellCasterTiers.CASTER_TIER.getName(),
                    new AttributeModifier(
                            UUID.randomUUID(),
                            "Caster Modifier",
                            this.tier, AttributeModifier.Operation.ADDITION
                    )
            );
        }
        return multimap;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entityLiving, int count)
    {
        if (!(entityLiving instanceof PlayerEntity))
            return;
        // Fire pre persistent spell event, if cancelled, spell will not be executed

        if (!entityLiving.getEntityWorld().isRemote() && MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Pre(entityLiving, true)))
        {
            ServerWorld worldIn = (ServerWorld) entityLiving.getEntityWorld();
            SpellCasterSource source = new SpellCasterSource(worldIn, entityLiving, null, tier);
            ITranslatedSpellProvider spellProvider = getSpellProvider((PlayerEntity) entityLiving, getSpellSlot(stack));
            // Common sentences will be only executed once!
            List<ParseResults<SpellCasterSource>> parseResults =
                    spellProvider.getCompileResults(source);
            // OnHold sentences will be executed every tick
            SpellDispatcher.executeSpell(parseResults);
            // Fire post persistent spell event
            MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Post(entityLiving, true));
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
    private static ITranslatedSpellProvider getSpellProvider(PlayerEntity player, int slot)
    {
        ISpellInventory inventory = player.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY).orElse(new SpellInventory());
        ItemStack itemSpellStack = inventory.getShortcut(slot);
        if (itemSpellStack.getItem() instanceof ItemSpell)
            return ((ItemSpell) itemSpellStack.getItem()).getSpellProvider(itemSpellStack);
        return new ITranslatedSpellProvider.Impl();
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
