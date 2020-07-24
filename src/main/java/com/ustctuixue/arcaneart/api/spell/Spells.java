package com.ustctuixue.arcaneart.api.spell;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellDispatcher;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@SuppressWarnings("WeakerAccess")
public class Spells
{
    private static Marker REGISTER_SPELL = MarkerManager.getMarker("REGISTER SPELL");
    public static void registerAll(SpellDispatcher dispatcher)
    {
        new DefineVariable(dispatcher);
    }

    public static LiteralArgumentBuilder<SpellCasterSource> literal(SpellKeyWord keyWord)
    {
        if (!SpellKeyWord.REGISTRY.containsValue(keyWord))
        {
            ArcaneArtAPI.LOGGER.error(REGISTER_SPELL,
                    "Keyword \"" + keyWord.toString() + "\" is not registered."
            );
        }
        else if (keyWord.getType() == SpellKeyWord.ExecuteType.NOT_EXECUTABLE)
        {
            ArcaneArtAPI.LOGGER.error(REGISTER_SPELL,
                    "Keyword \"" + keyWord.toString() + "\" is not executable."
            );
        }
        return LiteralArgumentBuilder.literal(keyWord.toString());
    }

    /**
     * Creates a new argument. Intended to be imported statically. The benefit of this over the brigadier {@link
     * RequiredArgumentBuilder#argument} method is that it is typed to {@link SpellCasterSource}.
     */
    public static <T> RequiredArgumentBuilder<SpellCasterSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static class DefineVariable
    {
        DefineVariable(SpellDispatcher dispatcher)
        {
            dispatcher.register(literal(SpellKeyWords.MAKE)
                    .then(argument("variableName", StringArgumentType.string())
                            .then(argument("variableEntityList", new EntityListArgument())
                                    .executes(context -> makeRelativeVariable(
                                            context.getArgument("variableName", String.class),
                                            context.getArgument("variableEntityList", RelativeEntityListBuilder.class),
                                            context.getSource()
                                            )
                                    )
                            )
                            .then(argument("variableLocationList", new RelativeVec3dListArgument())
                                    .executes(context -> makeRelativeVariable(
                                            context.getArgument("variableName", String.class),
                                            context.getArgument("variableLocationList", RelativeVec3dListBuilder.class),
                                            context.getSource()
                                            )
                                    )
                            )
                    )
            );
        }

        static <R, T extends IRelativeArgumentBuilder<R>> int makeRelativeVariable(String name, T builder, SpellCasterSource source)
        {
            R value = builder.build(source);
            source.setVariable(name, value);
            return 1;
        }
    }
}
