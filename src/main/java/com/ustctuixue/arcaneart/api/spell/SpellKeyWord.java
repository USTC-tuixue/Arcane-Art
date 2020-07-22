package com.ustctuixue.arcaneart.api.spell;

import joptsimple.internal.Strings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;

public class SpellKeyWord implements IForgeRegistryEntry<SpellKeyWord>
{
    public static ForgeRegistry<SpellKeyWord> REGISTRY;
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
            return String.format("kw:%04d", id);
        }
        return word;
    }

    static String decode(String word)
    {
        if (word.matches("kw:[0-9]{4}"))
        {
            SpellKeyWord keyWord = REGISTRY.getValue(Integer.getInteger(word.substring(3)));
            ResourceLocation rl = keyWord.getRegistryName();
            if (rl != null)
            {
                return rl.toString();
            }
        }
        return word;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SpellKeyWord)
        {
            return this.getRegistryName().equals(((SpellKeyWord) obj).getRegistryName());
        }
        return false;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return this.getRegistryName().toString();
    }
}
