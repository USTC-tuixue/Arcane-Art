package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class ItemSpell extends Item
{
    public ItemSpell(Properties properties)
    {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        if (stack.hasTag()) {
            CompoundNBT compoundnbt = stack.getTag();
            String s = compoundnbt.getString("title");
            if (!StringUtils.isNullOrEmpty(s)) {
                return new StringTextComponent(s);
            }
        }

        return super.getDisplayName(stack);
    }

    public static List<String> getStoredSpell(ItemStack stack)
    {
        if (stack.hasTag())
        {
            CompoundNBT nbt = stack.getTag();
            return (List<String>) ReflectHelper.getListNBTValues(nbt.getList("spell", 8), 8);
        }
        return Lists.newArrayList();
    }
}
