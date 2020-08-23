package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import lombok.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SpellKeyWord implements IForgeRegistryEntry<SpellKeyWord>
{
    public static ForgeRegistry<SpellKeyWord> REGISTRY
            = (ForgeRegistry<SpellKeyWord>) new RegistryBuilder<SpellKeyWord>()
            .setType(SpellKeyWord.class)
            .setName(ArcaneArtAPI.getResourceLocation("spell_words"))
            .create();

    @Getter
    private final ExecuteType type;

    public SpellKeyWord (Property property)
    {
        this.type = property.type;
    }



    public final String getTranslationPath()
    {
        return name.getNamespace() + "." + name.getPath();
    }

    /////////////////////////////////////////
    // Registry Start
    /////////////////////////////////////////
    private ResourceLocation name;

    @Override
    public final SpellKeyWord setRegistryName(ResourceLocation name)
    {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public final ResourceLocation getRegistryName()
    {
        return this.name;
    }


    @Override
    public final Class<SpellKeyWord> getRegistryType()
    {
        return SpellKeyWord.class;
    }

    static String encode(String word)
    {
        int id = REGISTRY.getID(new ResourceLocation(word));
        if (id != -1)
        {
            return String.format("kw:%04x", id);
        }
        return word;
    }

    static String decode(String word)
    {
        if (word.matches("kw:[0-9a-f]{4}"))
        {
            LogManager.getLogger(SpellKeyWord.class).debug("Word id: 0x" + word.substring(3));
            SpellKeyWord keyWord = REGISTRY.getValue(Integer.decode("0x" + word.substring(3)));
            ResourceLocation rl = keyWord.getRegistryName();
            if (rl != null)
            {
                return rl.toString();
            }
        }
        return word;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SpellKeyWord)
        {
            return this.getRegistryName().equals(((SpellKeyWord) obj).getRegistryName());
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.getRegistryName().toString();
    }


    //////////////////////////////////////////////////////////////////
    // Properties
    //////////////////////////////////////////////////////////////////


    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("WeakerAccess")
    public static class Property
    {
        @Getter @With @Nonnull
        private ExecuteType type = ExecuteType.NOT_EXECUTABLE;

        public Supplier<? extends SpellKeyWord> getSupplier()
        {
            return () -> new SpellKeyWord(this);
        }

    }

    public enum ExecuteType
    {

        PRE_PROCESS("preprocess"), ON_HOLD("onHold"), ON_RELEASE("onRelease"), NOT_EXECUTABLE("notExecutable");

        String name;
        ExecuteType(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }
    }
}
