package com.ustctuixue.arcaneart.spell.modifier;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.modifier.ISpellCostModifier;
import com.ustctuixue.arcaneart.spell.SpellConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ArmorModifier implements ISpellCostModifier
{
    @Override
    public double getAmplifier(double originalCost, SpellCasterSource source)
    {
        Entity entity = source.getEntity();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;
            double enchantability = getAverageEnchantability(player);
            return SpellConfig.SpellModifiers.getArmorEnchantabilityModifier(enchantability);
        }

        return 1;
    }

    private double getAverageEnchantability(@Nonnull PlayerEntity player)
    {
        double enchantability = 0;
        int totalDurability = 0;
        for (ItemStack armorStack: player.getArmorInventoryList())
        {
            if (armorStack.getItem() instanceof ArmorItem)
            {
                ArmorItem armorItem = (ArmorItem) armorStack.getItem();
                IArmorMaterial material = armorItem.getArmorMaterial();
                EquipmentSlotType slotType = armorItem.getEquipmentSlot(armorStack);
                if (slotType != null && material.getDurability(slotType) != 0)
                {
                    enchantability += material.getEnchantability() * material.getDurability(slotType);
                    totalDurability += material.getDurability(slotType);
                }
            }
        }
        if (totalDurability == 0)
        {
            return SpellConfig.SpellModifiers.ARMOR_ENCHANTABILITY_DEFAULT.get();
        }
        return enchantability / totalDurability;
    }
}
