package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.text.TranslationTextComponent;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandExceptionTypes
{
    static String getExceptionTranslationKey(String name)
    {
        return "spell.exception." + name;
    }

    public static final SimpleCommandExceptionType INVALID_SPELL_WORD
            = new SimpleCommandExceptionType(new TranslationTextComponent(getExceptionTranslationKey("invalid_spell_word")));

}
