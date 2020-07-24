package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.ustctuixue.arcaneart.api.spell.CapabilitySpell;
import com.ustctuixue.arcaneart.api.spell.ItemSpell;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class Interpreter<CASTER>
{
    protected abstract SpellCasterSource getCasterSource(CASTER caster);
    public boolean checkSpell(TranslatedSpell spell, CASTER caster)
    {
        return SpellDispatcher.checkSpell(spell, getCasterSource(caster));
    }

    public ItemStack getItemSpell(TranslatedSpell spell, CASTER caster, @Nonnull ItemSpell itemSpell)
    {
        if (checkSpell(spell, caster))
        {
            ItemStack stack = new ItemStack(itemSpell);
            itemSpell.setSpell(stack, spell);
            return stack;
        }
        return ItemStack.EMPTY;
    }

}
