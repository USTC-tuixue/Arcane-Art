package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ItemSpell extends Item
{
    public ItemSpell(Properties properties)
    {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return new StringTextComponent(getSpell(stack).getName());
    }

    public TranslatedSpell getSpell(ItemStack stack)
    {
        return stack.getCapability(CapabilitySpell.SPELL_CAP).orElseGet(TranslatedSpell::new);
    }
}
