package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;
import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilitySpell
{
    @CapabilityInject(Spell.class)
    public static Capability<Spell> SPELL_CAP = null;


    public static class Storage implements Capability.IStorage<Spell>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<Spell> capability, Spell instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("title", instance.getName());

            ListNBT list = new ListNBT();
            encodeIncantation(instance.getIncantations()).forEach(s -> list.add(StringNBT.valueOf(s)));
            nbt.put("incantations", list);
            return nbt;
        }

        @Override @SuppressWarnings("unchecked")
        public void readNBT(Capability<Spell> capability, Spell instance, Direction side, INBT nbt)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            SpellBuilder builder = new SpellBuilder()
                    .withName(compoundNBT.getString("title"))
                    .withIncantations(decodeIncantation(
                            (List<String>)
                                    ReflectHelper.getListNBTValues(
                                            compoundNBT.getList("incantations", 8),
                                            8
                                    )
                    ));
            Spell spell = builder.build();
            instance.copyFrom(spell);
        }

        // Compress incantations
        private List<String> encodeIncantation(List<String> incantations)
        {
            return incantations.stream().map(s ->
                {
                    String[] words = s.split(" ");
                    for (int i = 0; i < words.length; ++i)
                    {
                        words[i] = SpellKeyWord.encode(words[i]);
                    }
                    return String.join(" ", words);
                }
            ).collect(Collectors.toList());
        }

        private List<String> decodeIncantation(List<String> encoded)
        {
            return encoded.stream().map(s ->
            {
                String[] words = s.split(" ");
                for (int i = 0; i < words.length; i++)
                {
                    words[i] = SpellKeyWord.decode(words[i]);
                }
                return String.join(" ", words);
            }
            ).collect(Collectors.toList());
        }
    }

    public static class StorageProvider implements ICapabilitySerializable<CompoundNBT>
    {
        Storage storage = new Storage();
        Spell spell = new SpellBuilder().build();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CAP ? LazyOptional.of(()->spell).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) storage.writeNBT(SPELL_CAP, spell, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            storage.readNBT(SPELL_CAP, spell, null, nbt);
        }
    }

    public static class Provider implements ICapabilityProvider
    {
        Spell spell = new SpellBuilder().build();
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CAP ? LazyOptional.of(()->spell).cast() : LazyOptional.empty();
        }
    }
}
