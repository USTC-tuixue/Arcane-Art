package com.ustctuixue.arcaneart.api.spell.interpreter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.ustctuixue.arcaneart.api.mp.IMPConsumer;
import com.ustctuixue.arcaneart.api.spell.TranslatedSpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.tree.SpellLiteralCommandNode;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

import java.util.List;
import java.util.stream.Collectors;

public class SpellDispatcher extends CommandDispatcher<SpellCasterSource>
{
    private static SpellDispatcher DISPATCHER = new SpellDispatcher();

    /**
     * Check if spell get any errors
     * @param spell spell to check
     * @param source spell caster source
     * @return true for no errors
     */
    public boolean checkSpellSentence(String spell, SpellCasterSource source)
    {
        return parse(spell, source).getExceptions().size() == 0;
    }


    @Override
    public int execute(ParseResults<SpellCasterSource> parse) throws CommandSyntaxException
    {
        SpellCasterSource spellCasterSource = parse.getContext().getSource();
        IMPConsumer consumer = spellCasterSource.getMpConsumer();
        List<ParsedCommandNode<SpellCasterSource>> nodes = parse.getContext().getNodes();
        ISpellEffectProvider spellSideEffectProvider = null;

        for (ParsedCommandNode<SpellCasterSource> spellCasterSourceParsedCommandNode : nodes)
        {
            CommandNode<SpellCasterSource> node = spellCasterSourceParsedCommandNode.getNode();
            if (node instanceof SpellLiteralCommandNode)
            {
                if (((SpellLiteralCommandNode) node).hasSideEffectProvider())
                {
                    spellSideEffectProvider = ((SpellLiteralCommandNode) node).getSpellEffectProvider();
                    break;
                }
            }
        }

        if (spellSideEffectProvider != null)
        {
            SpellSideEffect spellSideEffect = spellSideEffectProvider.build(spellCasterSource);
            if (consumer.consumeMana(spellSideEffect.getMpCost()))
            {
                return super.execute(parse);
            }
        }
        else
        {
            return super.execute(parse);
        }


        return 0;
    }

    public static boolean checkSpell(TranslatedSpell spell, SpellCasterSource source)
    {
        final boolean[] f = {true};
        spell.getCommonSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        spell.getOnHoldSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        spell.getOnReleaseSentences().forEach(s -> f[0] = f[0] && DISPATCHER.checkSpellSentence(s, source));
        return f[0];
    }

    public static class NewSpellEvent extends ServerLifecycleEvent
    {
        public NewSpellEvent(MinecraftServer server)
        {
            super(server);
        }

        public SpellDispatcher getDispatcher()
        {
            return DISPATCHER;
        }
    }

    public static int executeSpell(String spell, SpellCasterSource source)
    {
        try
        {
            return DISPATCHER.execute(DISPATCHER.parse(spell, source));
        }catch (CommandSyntaxException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static int executeSpell(List<String> spells, SpellCasterSource source)
    {
        boolean f = true;
        for (String spell : spells)
        {
            f = f && executeSpell(spell, source) != 0;
        }
        return f ? 1 : 0;
    }

    public static List<ParseResults<SpellCasterSource>> parseSpell(List<String> spells, SpellCasterSource source)
    {
        return spells.stream().map(spell -> DISPATCHER.parse(spell, source)).collect(Collectors.toList());
    }

    public static int executeSpell(List<ParseResults<SpellCasterSource>> parseResults)
    {
        boolean f = true;
        for (ParseResults<SpellCasterSource> parseResult : parseResults)
        {
            try
            {
                f = f && DISPATCHER.execute(parseResult) != 0;
            } catch (CommandSyntaxException e)
            {
                e.printStackTrace();
            }
        }
        return f ? 1 : 0;
    }

}
