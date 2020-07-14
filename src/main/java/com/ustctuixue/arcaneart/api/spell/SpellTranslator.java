package com.ustctuixue.arcaneart.api.spell;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ustctuixue.arcaneart.ArcaneArt;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import org.lwjgl.system.CallbackI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public static String expandMacros(List<String> list)
    {
        Map<String, String> macroMap = Maps.newHashMap();
        for (String line : list)
        {
            String[] words = line.split(" ");
            for (int i = 0; i < words.length; i++)
            {
                words[i] = macroMap.getOrDefault(words[i], words[i]);

            }
            if (line.matches("[lL]et \\S* be .*"))
            {
                macroMap.put(words[1], line.split("be ")[1]);
            }
        }
        return list.get(list.size() - 1);
    }


}
