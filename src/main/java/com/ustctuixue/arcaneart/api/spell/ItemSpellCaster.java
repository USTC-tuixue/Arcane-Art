package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Multimap;
import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import com.ustctuixue.arcaneart.api.mp.CapabilityMP;
import com.ustctuixue.arcaneart.api.mp.DefaultManaBar;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.mp.MPEvent;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellContainer;
import com.ustctuixue.arcaneart.api.spell.inventory.*;
import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

        if (!worldIn.isRemote)
        {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            SpellCasterSource source = new SpellCasterSource(serverWorld, entityLiving, null, tier);
            ITranslatedSpellProvider spellProvider = getSpellProvider((PlayerEntity) entityLiving, getSpellSlot(stack));

            // Get compiled spell
            SpellContainer container = spellProvider.getCompiled(source);

            // Get mana cost
            double cost = SpellContainer.getManaCost(source, container.preProcess);
            cost += SpellContainer.getManaCost(source, container.onRelease);
            // Get complexity
            double complexity = SpellContainer.getComplexity(source, container.preProcess);
            complexity += SpellContainer.getComplexity(source,container.onRelease);

            // Fire pre instant spell event, will not be executed nor consume mana if cancelled
            if (MinecraftForge.EVENT_BUS.post(
                    new MPEvent.CastSpell.Pre(
                            entityLiving, false, cost, complexity
                    )
            ) && source.getMpConsumer().consumeMana(cost))
            {
                container.executePreProcess(source);
                container.executeOnRelease(source);
                // Fire post instant spell event
                MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Post(
                        entityLiving, true, cost, complexity
                ));
            }
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
            multimap.put(CapabilityMP.CASTER_TIER.getName(),
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

        if (!entityLiving.getEntityWorld().isRemote())
        {
            ServerWorld worldIn = (ServerWorld) entityLiving.getEntityWorld();
            SpellCasterSource source = new SpellCasterSource(worldIn, entityLiving, null, tier);
            ITranslatedSpellProvider spellProvider = getSpellProvider((PlayerEntity) entityLiving, getSpellSlot(stack));

            // Get compiled spell
            SpellContainer container = spellProvider.getCompiled(source);
            IManaBar manaBar = entityLiving.getCapability(CapabilityMP.MANA_BAR_CAP).orElseGet(DefaultManaBar::new);

            // Get cost
            double cost = SpellContainer.getManaCost(source, container.preProcess);
            cost += SpellContainer.getManaCost(source, container.onHold);
            // Get complexity
            double complexity = SpellContainer.getComplexity(source, container.preProcess);
            complexity += SpellContainer.getComplexity(source,container.onHold);

            // Fire pre persistent spell event, if cancelled, spell will not be executed nor cost mana
            if (
                    MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Pre(
                            entityLiving, true, cost, complexity
                            )
                    )
                            && manaBar.consumeMana(cost)
                    && manaBar.canTolerate(complexity, entityLiving)
            )
            {
                container.executePreProcess(source);
                container.executeOnHold(source);
                // Fire post persistent spell event
                MinecraftForge.EVENT_BUS.post(new MPEvent.CastSpell.Post(
                        entityLiving, true, cost, complexity
                ));
            }
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

    public static ItemStack getSpellStack(ItemStack casterStack, PlayerEntity player)
    {
        int slot = getSpellSlot(casterStack);
        ISpellInventory inventory = player.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY).orElse(new SpellInventory());
        return inventory.getShortcut(slot);
    }

    @Nonnull
    public static ITranslatedSpellProvider getSpellProvider(PlayerEntity player, int slot)
    {
        ISpellInventory inventory = player.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY).orElse(new SpellInventory());
        ItemStack itemSpellStack = inventory.getShortcut(slot);
        if (itemSpellStack.getItem() instanceof ItemSpell)
            return ((ItemSpell) itemSpellStack.getItem()).getSpellProvider(itemSpellStack);
        return new ITranslatedSpellProvider.Impl();
    }

    public static void setSpellSlot(ItemStack stack, int slot)
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

    public static int getSpellSlot(ItemStack stack)
    {
        if (stack.getTag() != null)
        {
            byte slot = stack.getTag().getByte("spell_slot");
            if (slot >= 0 && slot < 9)
            {
                return slot;
            }
        }
        setSpellSlot(stack, 0);    // If not a valid spell slot id, set to 0
        return 0;
    }
}
