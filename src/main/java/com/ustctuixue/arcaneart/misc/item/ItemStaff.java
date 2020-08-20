package com.ustctuixue.arcaneart.misc.item;

import com.ustctuixue.arcaneart.ArcaneArt;
import com.ustctuixue.arcaneart.api.spell.ItemSpellCaster;

import net.minecraft.item.Item.Properties;

public class ItemStaff extends ItemSpellCaster{

	public ItemStaff() {
		super(new Properties().group(ArcaneArt.ARCANE_ART_ITEM_GROUP).maxStackSize(1),10);
			// TODO Auto-generated constructor stub
	}

}
