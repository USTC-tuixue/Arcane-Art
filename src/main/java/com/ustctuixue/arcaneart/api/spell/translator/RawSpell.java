package com.ustctuixue.arcaneart.api.spell.translator;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import org.apache.logging.log4j.LogManager;

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
            System.out.println(pages.toString());
            StringBuilder buffer = new StringBuilder();
            for (INBT page : pages)
            {
                String pageContent;
                if (page instanceof StringNBT)
                {
                    pageContent = page.toString();
                    pageContent = pageContent.substring(1, pageContent.length() - 1);
                    LogManager.getLogger(RawSpell.class).info("Page Content: " + pageContent);
                    try
                    {
                        CompoundNBT pageNBT = JsonToNBT.getTagFromJson(pageContent);    // Value in pages tag is Json Text
                        if (pageNBT.contains("translate"))
                        {
                            pageContent = pageNBT.getString("translate");
                        }
                        else if (pageNBT.contains("text"))
                        {
                        	System.out.println("???");
                            pageContent = pageNBT.getString("text");
                        }
                        else
                        {
                            break;                                                      // No available spell text
                        }
                    }catch (CommandSyntaxException e)
                    {
                        break;
                    }
                }
                else
                {
                    break;
                }

                if (!pageContent.endsWith("-")) // 连字符

                {
                    buffer.append(" ");
                }
                buffer.append(pageContent);
            }

            return new RawSpell(
                    Items.WRITTEN_BOOK.getDisplayName(itemStack).getFormattedText(),
                    buffer.toString().replaceAll("-", "")
                    .replaceAll("\\\\n", " ")
            );


        }
        return null;
    }
}