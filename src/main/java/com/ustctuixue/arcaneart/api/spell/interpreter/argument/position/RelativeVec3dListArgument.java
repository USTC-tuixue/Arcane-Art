package com.ustctuixue.arcaneart.api.spell.interpreter.argument.position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.RelativeEntityListBuilder;

/**
 * Also accept position of the first entity in EntityList
 */
public class RelativeVec3dListArgument implements ArgumentType<RelativeVec3dListBuilder>
{
    @Override
    public RelativeVec3dListBuilder parse(StringReader reader) throws CommandSyntaxException
    {

        try{
            RelativeEntityListBuilder builder = new EntityListArgument().parse(reader);
            return new RelativeVec3dListBuilder(builder);
        }catch (CommandSyntaxException e)
        {
            double x, y, z;
            ArcaneArtAPI.LOGGER.debug(reader.getRemaining());
            reader.skipWhitespace();
            x = reader.readDouble();
            reader.skipWhitespace();
            y = reader.readDouble();
            reader.skipWhitespace();
            z = reader.readDouble();
            return new RelativeVec3dListBuilder(x, y, z);
        }

    }
}
