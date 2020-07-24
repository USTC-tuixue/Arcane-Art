package com.ustctuixue.arcaneart.api.spell.interpreter.builder;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpellEffectProvider;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.tree.SpellLiteralCommandNode;
import lombok.Getter;

public class SpellLiteralCommandNodeBuilder extends LiteralArgumentBuilder<SpellCasterSource>
{
    @Getter
    private ISpellEffectProvider spellEffectProvider;

    protected SpellLiteralCommandNodeBuilder(SpellKeyWord keyWord)
    {
        super(keyWord.toString());
    }

    public static SpellLiteralCommandNodeBuilder literal(SpellKeyWord keyWord)
    {
        return new SpellLiteralCommandNodeBuilder(keyWord);
    }

    public SpellLiteralCommandNodeBuilder sideEffect(ISpellEffectProvider provider)
    {
        this.spellEffectProvider = provider;
        return this;
    }

    @Override
    public SpellLiteralCommandNode build()
    {
        final SpellLiteralCommandNode result = new SpellLiteralCommandNode(
                getLiteral(), getCommand(), getRequirement(),
                getSpellEffectProvider(),
                getRedirect(), getRedirectModifier(), isFork()
        );

        for (final CommandNode<SpellCasterSource> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
