package com.ustctuixue.arcaneart.api.spell;

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
    @CapabilityInject(TranslatedSpell.class)
    public static Capability<TranslatedSpell> SPELL_CAP = null;


    public static class Storage implements Capability.IStorage<TranslatedSpell>
    {
        private static final String TITLE = "title";
        private static final String COMMON = "commonSentences";
        private static final String ON_HOLD = "onHoldSentences";
        private static final String ON_RELEASE = "onReleaseSentences";

        @Nullable
        @Override
        public INBT writeNBT(Capability<TranslatedSpell> capability, TranslatedSpell instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString(TITLE, instance.getName());
            nbt.put(COMMON, encodeIncantation(instance.getCommonSentences()));
            nbt.put(ON_HOLD, encodeIncantation(instance.getOnHoldSentences()));
            nbt.put(ON_RELEASE, encodeIncantation(instance.getOnReleaseSentences()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<TranslatedSpell> capability, TranslatedSpell instance, Direction side, INBT nbt)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            instance.setName(compoundNBT.getString(TITLE));
            instance.addAllCommonSentences(decodeIncantation(compoundNBT.getList(COMMON, 8)));
            instance.addAllOnHoldSentences(decodeIncantation(compoundNBT.getList(ON_HOLD, 8)));
            instance.addAllOnHoldSentences(decodeIncantation(compoundNBT.getList(ON_RELEASE, 8)));
        }

        // Compress incantations
        private ListNBT encodeIncantation(List<String> incantations)
        {
            ListNBT nbt = new ListNBT();
            incantations.forEach(s ->
                {
                    String[] words = s.split(" ");
                    for (int i = 0; i < words.length; ++i)
                    {
                        words[i] = SpellKeyWord.encode(words[i]);
                    }
                    nbt.add(StringNBT.valueOf(String.join(" ", words)));
                }
            );
            return nbt;
        }

        private List<String> decodeIncantation(ListNBT encoded)
        {

            return encoded.stream().map(stringNBT ->
            {
                String sentence = stringNBT.getString();
                String[] words = sentence.split(" ");
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
        TranslatedSpell spell = new TranslatedSpell();

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
        TranslatedSpell spell = new TranslatedSpell();
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CAP ? LazyOptional.of(()->spell).cast() : LazyOptional.empty();
        }
    }
}
