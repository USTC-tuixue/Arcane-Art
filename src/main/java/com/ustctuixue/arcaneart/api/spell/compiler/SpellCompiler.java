package com.ustctuixue.arcaneart.api.spell.compiler;

import com.ustctuixue.arcaneart.api.spell.Spell;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageManager;
import com.ustctuixue.arcaneart.api.spell.translator.LanguageProfile;
import com.ustctuixue.arcaneart.api.spell.translator.SpellTranslator;
import com.ustctuixue.arcaneart.api.spell.translator.RawSpell;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SpellCompiler
{
    public static Spell compileFromBook(ItemStack stack)
    {
        if (stack.getItem() == Items.WRITTEN_BOOK)
        {
            RawSpell rawSpell = SpellTranslator.joinFromWrittenBook(stack);
            if (rawSpell == null)
            {
                return null;
            }
            LanguageProfile profile = LanguageManager.getInstance().getBestMatchedProfile(rawSpell.getIncantations());
            if (profile != null)
            {
                SpellBuilder builder = new SpellBuilder();
                builder.withIncantations(SpellTranslator.translateByProfile(rawSpell.getIncantations(), profile));
                builder.withName(rawSpell.getName());
                Spell spell = builder.build();
                return spell.hasEffect()?spell:null;
            }
        }
        return null;
    }
}
