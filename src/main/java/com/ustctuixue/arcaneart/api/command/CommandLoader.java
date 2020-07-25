package com.ustctuixue.arcaneart.api.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class CommandLoader
{
    public static void registerAll(CommandDispatcher<CommandSource> dispatcher)
    {
        new CastSpellCommand(dispatcher);
    }
}
