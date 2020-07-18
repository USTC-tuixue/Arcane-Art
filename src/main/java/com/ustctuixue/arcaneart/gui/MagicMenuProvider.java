package com.ustctuixue.arcaneart.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MagicMenuProvider implements INamedContainerProvider{

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
		return new MagicContainer(id,inv,player);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("title.arcaneart.magicmenu");
	}

}
