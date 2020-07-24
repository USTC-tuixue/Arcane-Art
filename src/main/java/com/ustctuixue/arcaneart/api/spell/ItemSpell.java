package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class ItemSpell extends Item
{
    public ItemSpell(Properties properties)
    {
        super(properties);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName(@Nonnull ItemStack stack)
    {
        return new StringTextComponent(getSpell(stack).getName());
    }

    public TranslatedSpell getSpell(ItemStack stack)
    {
        return stack.getCapability(CapabilitySpell.SPELL_CAP).orElseGet(ITranslatedSpellProvider.Impl::new).getSpell();
    }

    public void setSpell(ItemStack stack, TranslatedSpell spell)
    {
        stack.getCapability(CapabilitySpell.SPELL_CAP).ifPresent( spellProvider ->
                spellProvider.setSpell(spell)
        );
    }
}
