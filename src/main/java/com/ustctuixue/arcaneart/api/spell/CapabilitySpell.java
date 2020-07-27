package com.ustctuixue.arcaneart.api.spell;

import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilitySpell
{
    @SuppressWarnings("WeakerAccess")
    @CapabilityInject(ITranslatedSpellProvider.class)
    public static Capability<ITranslatedSpellProvider> SPELL_CAP = null;


    public static class Storage implements Capability.IStorage<ITranslatedSpellProvider>
    {
        private static final String TITLE = "title";
        private static final String PRE_PROCESS = "preProcessSentences";
        private static final String ON_HOLD = "onHoldSentences";
        private static final String ON_RELEASE = "onReleaseSentences";

        @Nullable
        @Override
        public INBT writeNBT(Capability<ITranslatedSpellProvider> capability, ITranslatedSpellProvider instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            TranslatedSpell translatedSpell = instance.getSpell();
            nbt.putString(TITLE, translatedSpell.getName());
            nbt.put(PRE_PROCESS, encodeIncantation(translatedSpell.getPreProcessSentences()));
            nbt.put(ON_HOLD, encodeIncantation(translatedSpell.getOnHoldSentences()));
            nbt.put(ON_RELEASE, encodeIncantation(translatedSpell.getOnReleaseSentences()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<ITranslatedSpellProvider> capability, ITranslatedSpellProvider instance, Direction side, INBT nbt)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            TranslatedSpell translatedSpell = instance.getSpell();
            translatedSpell.setName(compoundNBT.getString(TITLE));
            translatedSpell.addAllCommonSentences(decodeIncantation(compoundNBT.getList(PRE_PROCESS, 8)));
            translatedSpell.addAllOnHoldSentences(decodeIncantation(compoundNBT.getList(ON_HOLD, 8)));
            translatedSpell.addAllOnHoldSentences(decodeIncantation(compoundNBT.getList(ON_RELEASE, 8)));
            instance.setSpell(translatedSpell);
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
        ITranslatedSpellProvider provider = new ITranslatedSpellProvider.Impl(spell);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CAP ? LazyOptional.of(()->provider).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) storage.writeNBT(SPELL_CAP, provider, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            storage.readNBT(SPELL_CAP, provider, null, nbt);
        }
    }

}
