package com.ustctuixue.arcaneart.gui.magicmenu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class MagicMenuProvider implements INamedContainerProvider
{

    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player)
    {
        return new MagicContainer(id, inv, player);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("title.arcaneart.magicmenu");
    }

}
