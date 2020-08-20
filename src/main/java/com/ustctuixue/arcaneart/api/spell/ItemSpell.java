package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.spell.interpreter.SpellContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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

    @SuppressWarnings("WeakerAccess")
    public TranslatedSpell getSpell(ItemStack stack)
    {
        return getSpellProvider(stack).getSpell();
    }

    @SuppressWarnings("WeakerAccess")
    public ITranslatedSpellProvider getSpellProvider(ItemStack stack)
    {
        LazyOptional<ITranslatedSpellProvider> provider = stack.getCapability(CapabilitySpell.SPELL_CAP).cast();
        return provider.orElse(new ITranslatedSpellProvider.Impl());
    }

    public void setSpell(ItemStack stack, TranslatedSpell spell, SpellContainer container)
    {
        stack.getCapability(CapabilitySpell.SPELL_CAP).ifPresent( spellProvider ->
                {
                    spellProvider.setSpell(spell);
                    spellProvider.setCompiled(container);
                }
        );
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        SpellAuthor author = this.getSpell(stack).getAuthor();
        String s = null;
        if (author != null)
        {
            s = author.getName();
        }
        if (!StringUtils.isNullOrEmpty(s)) {
            tooltip.add((new TranslationTextComponent("book.byAuthor", s)).applyTextStyle(TextFormatting.GRAY));
        }
    }
}
