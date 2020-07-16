package com.ustctuixue.arcaneart.api.spell;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellTranslator
{
    public static List<String> jointFromWrittenBook(ItemStack itemStack)
    {
        CompoundNBT compoundNBT = itemStack.getTag();
        if (compoundNBT != null)
        {
            ListNBT pages = itemStack.getTag().getList("pages", 8);
            StringBuilder buffer = new StringBuilder();
            for (INBT page : pages)
            {
                String pageContent = ((StringNBT)page).getString();
                if (!pageContent.endsWith("-"))
                {
                    buffer.append(" ");
                }
                buffer.append(pageContent);
            }

            return Arrays.asList(buffer.toString().replaceAll("[-\n]", "").split(". "));


        }
        return null;
    }

    private static Map<String, String> KEY_WORD_TRANSLATION = Maps.newHashMap();

    public static List<String> translateNaturalToStandard(List<String> incantations)
    {
        List<String> machineLanguage = Lists.newArrayList();
        for (String key :
                KEY_WORD_TRANSLATION.keySet())
        {
            for (String incantation : incantations)
            {
                machineLanguage.add(
                        incantation.replaceAll(KEY_WORD_TRANSLATION.get(key), key)
                );
            }
        }

        return machineLanguage;
    }



}
