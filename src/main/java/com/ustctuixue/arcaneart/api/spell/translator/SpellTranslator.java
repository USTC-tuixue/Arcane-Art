package com.ustctuixue.arcaneart.api.spell.translator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpellTranslator
{
    public static List<String> joinFromWrittenBook(ItemStack itemStack)
    {
        CompoundNBT compoundNBT = itemStack.getTag();
        if (compoundNBT != null)
        {
            ListNBT pages = itemStack.getTag().getList("pages", 8);
            StringBuilder buffer = new StringBuilder();
            for (INBT page : pages)
            {
                String pageContent = page.getString();
                if (!pageContent.endsWith("-")) // 连q

                {
                    buffer.append(" ");
                }
                buffer.append(pageContent);
            }

            return Arrays.asList(buffer.toString().replaceAll("[-\n]", "").split(". "));


        }
        return null;
    }

    public static List<String> translateByProfile(List<String> rawSpell, LanguageProfile profile)
    {
        return rawSpell.stream().map(profile::translate).collect(Collectors.toList());
    }
}
