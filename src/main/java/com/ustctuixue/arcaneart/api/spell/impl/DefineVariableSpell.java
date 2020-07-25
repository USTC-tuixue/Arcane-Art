package com.ustctuixue.arcaneart.api.spell.impl;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import com.ustctuixue.arcaneart.api.spell.interpreter.ISpell;
import com.ustctuixue.arcaneart.api.spell.interpreter.SpellCasterSource;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.IRelativeArgumentBuilder;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist.EntityListArgument;
import com.ustctuixue.arcaneart.api.spell.interpreter.argument.position.RelativeVec3dListArgument;

public class DefineVariableSpell implements ISpell
{
    private IRelativeArgumentBuilder<?> builder;
    private String name;

    @Override
    public double getComplexity(SpellCasterSource source)
    {
        return 0;
    }

    @Override
    public double getManaCostBase(SpellCasterSource source)
    {
        return 0;
    }

    @Override
    public double getManaCost(SpellCasterSource source)
    {
        return 0;   // 不受因素影响
    }

    @Override
    public void execute(SpellCasterSource source)
    {
        source.setVariable(this.name, this.builder.build(source));
    }

    /**
     * parse spell into self
     *
     * @param reader reader
     * @return true if successfully parsed
     */
    @Override
    public boolean parse(StringReader reader)
    {
        try
        {
            ArcaneArtAPI.LOGGER.debug(reader.getRemaining());
            reader.skipWhitespace();
            name = StringArgumentType.string().parse(reader);
            ArcaneArtAPI.LOGGER.debug("name: " + name);
            ArcaneArtAPI.LOGGER.debug(reader.getRemaining());
            try{
                builder = new EntityListArgument().parse(reader);
            }
            catch (CommandSyntaxException e)
            {
                try {
                    builder = new RelativeVec3dListArgument().parse(reader);
                }
                catch (CommandSyntaxException e1)
                {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
