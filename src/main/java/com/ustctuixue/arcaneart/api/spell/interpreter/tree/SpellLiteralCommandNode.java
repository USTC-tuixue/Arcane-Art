package com.ustctuixue.arcaneart.api.spell.interpreter.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpellEffectProvider;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import lombok.Getter;

import java.util.function.Predicate;

public class SpellLiteralCommandNode extends LiteralCommandNode<SpellCasterSource>
{
    @Getter
    private final ISpellEffectProvider spellEffectProvider;

    public SpellLiteralCommandNode(
            String literal, Command<SpellCasterSource> command, Predicate<SpellCasterSource> requirement,
            ISpellEffectProvider spellEffectProviderIn,
            CommandNode<SpellCasterSource> redirect, RedirectModifier<SpellCasterSource> modifier, boolean forks
    )
    {
        super(literal, command, requirement, redirect, modifier, forks);
        this.spellEffectProvider = spellEffectProviderIn;
    }

    public boolean hasSideEffectProvider()
    {
        return spellEffectProvider == null;
    }
}
