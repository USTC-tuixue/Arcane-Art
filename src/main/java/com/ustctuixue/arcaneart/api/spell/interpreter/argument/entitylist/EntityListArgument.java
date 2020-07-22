package com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWord;
import com.ustctuixue.arcaneart.api.spell.SpellKeyWords;
import com.ustctuixue.arcaneart.api.spell.interpreter.CommandExceptionTypes;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.ArgumentUtil;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeBlockPosArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeBlockPosBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dBuilder;
import com.ustctuixue.arcaneart.api.util.MinMaxBound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Predicate;

public class EntityListArgument implements ArgumentType<RelativeEntityListBuilder>
{
    private static final Map<SpellKeyWord, Predicate<Entity>> ENTITY_PREDICATE
            = Maps.newHashMap();
    static
    {
        ENTITY_PREDICATE.putAll(
                ImmutableMap.of(
                        SpellKeyWords.NEAREST_ENTITY,     IEntityPredicate.ENTITY,
                        SpellKeyWords.NEAREST_ANIMAL,     IEntityPredicate.ANIMAL,
                        SpellKeyWords.NEAREST_ENEMY,      IEntityPredicate.ENEMY,
                        SpellKeyWords.NEAREST_ITEM,       IEntityPredicate.ITEM,
                        SpellKeyWords.NEAREST_PLAYER,     IEntityPredicate.PLAYER
                )
        );
        ENTITY_PREDICATE.putAll(
                ImmutableMap.of(
                        SpellKeyWords.NEAREST_PROJECTILE, IEntityPredicate.PROJECTILE,
                        SpellKeyWords.NEAREST_ALLY,       IEntityPredicate.ALLY,
                        SpellKeyWords.SELF,               IEntityPredicate.SELF
                )
        );
    }

    public static void addSelectorKeyWord(SpellKeyWord keyWord, IEntityPredicate selector)
    {
        ENTITY_PREDICATE.putIfAbsent(keyWord, selector);
    }

    public static void addAllSelectorKwyWord(Map<SpellKeyWord, IEntityPredicate> keyWordSelectorMap)
    {
        keyWordSelectorMap.forEach(ENTITY_PREDICATE::putIfAbsent);
    }



    @Override
    public RelativeEntityListBuilder parse(StringReader reader) throws CommandSyntaxException
    {

        RelativeEntityListBuilder list = new RelativeEntityListBuilder();
        if (reader.canRead())
        {
            list.setPredicate(getPredicate(reader));
            list.setDistance(getMinMaxDistance(reader));
            list.setOriginPos(getOriginPos(reader));
            list.setLimit(getNumberLimit(reader));
        }
        return list;
    }

    private static Predicate<Entity> getPredicate(StringReader reader) throws CommandSyntaxException
    {
        int cursor = reader.getCursor();
        Predicate<Entity> entityPredicate;
        ResourceLocation predicateWord = new ResourceLocation(reader.readStringUntil(' '));
        SpellKeyWord keyWord = SpellKeyWord.REGISTRY.containsKey(predicateWord)? SpellKeyWord.REGISTRY.getValue(predicateWord):null;
        if (keyWord == null)
        {
            reader.setCursor(cursor);
            throw CommandExceptionTypes.INVALID_SPELL_WORD.createWithContext(reader);
        }
        entityPredicate = ENTITY_PREDICATE.getOrDefault(keyWord, null);
        if (entityPredicate == null)
        {
            reader.setCursor(cursor);
            throw CommandExceptionTypes.INVALID_SPELL_WORD.createWithContext(reader);
        }
        return entityPredicate;
    }

    private static MinMaxBound<Double> getMinMaxDistance(StringReader reader) throws CommandSyntaxException
    {
        double minDistance, maxDistance;
        reader.skipWhitespace();
        int cursor = reader.getCursor();
        if (!ArgumentUtil.validateSpellKeyWord(reader, SpellKeyWords.IN_RANGE))
        {
            reader.setCursor(cursor);
            return MinMaxBound.unBounded();
        }
        minDistance = reader.readDouble();
        cursor = reader.getCursor();
        reader.skipWhitespace();
        if (reader.peek() == ',')
        {
            maxDistance = reader.readDouble();
        }
        else
        {
            reader.setCursor(cursor);
            maxDistance = minDistance;
            minDistance = 0;
        }
        return MinMaxBound.of(minDistance, maxDistance);
    }

    private static RelativeVec3dBuilder getOriginPos(StringReader reader) throws CommandSyntaxException
    {
        if (ArgumentUtil.validateSpellKeyWord(reader, SpellKeyWords.FROM))
        {
            return new RelativeVec3dArgument().parse(reader);
        }
        return new RelativeVec3dBuilder();
    }

    private static int getNumberLimit(StringReader reader) throws CommandSyntaxException
    {
        if (ArgumentUtil.validateSpellKeyWord(reader, SpellKeyWords.MAX_COUNT))
        {
            return reader.readInt();
        }
        return 1;
    }

}
