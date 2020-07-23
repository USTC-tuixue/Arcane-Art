package com.ustctuixue.arcaneart.api.spell.translator;

import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import javax.annotation.Nullable;

@Data
public class RawSpell
{
    final String name;

    final String incantations;

    public RawSpell(String name, String incantations)
    {
        this.name = name;
        this.incantations = incantations;
    }

    public static RawSpell namelessSpell(String incantations)
    {
        return new RawSpell("", incantations);
    }

    @Nullable
    public static RawSpell fromWrittenBook(ItemStack itemStack)
    {
        CompoundNBT compoundNBT = itemStack.getTag();
        if (compoundNBT != null)
        {
            ListNBT pages = itemStack.getTag().getList("pages", 8);
            StringBuilder buffer = new StringBuilder();
            for (INBT page : pages)
            {
                String pageContent = page.getString();
                if (!pageContent.endsWith("-")) // 连字符

                {
                    buffer.append(" ");
                }
                buffer.append(pageContent);
            }

            return new RawSpell(
                    Items.WRITTEN_BOOK.getDisplayName(itemStack).getFormattedText(),
                    buffer.toString().replaceAll("-", "")
            );


        }
        return null;
    }
}
