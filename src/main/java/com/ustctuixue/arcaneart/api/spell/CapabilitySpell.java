package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.spell.compiler.SpellBuilder;
import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CapabilitySpell
{
    @CapabilityInject(Spell.class)
    public static Capability<Spell> SPELL_CASTER_CAP = null;

    public static class Storage implements Capability.IStorage<Spell>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<Spell> capability, Spell instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("title", instance.getName());
            ListNBT listNBT = new ListNBT();
            instance.getIncantations().forEach(s -> listNBT.add(StringNBT.valueOf(s)));
            nbt.put("incantations", listNBT);
            return nbt;
        }

        @Override @SuppressWarnings("unchecked")
        public void readNBT(Capability<Spell> capability, Spell instance, Direction side, INBT nbt)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            SpellBuilder builder = new SpellBuilder()
                    .withName(compoundNBT.getString("title"))
                    .withIncantations(
                            (List<String>) ReflectHelper.getListNBTValues(
                                    compoundNBT.getList("incantations", 8),
                                    8
                            )
                    );
            Spell spell = builder.build();
            instance.copyFrom(spell);
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        Storage storage = new Storage();
        Spell spell = new SpellBuilder().build();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == SPELL_CASTER_CAP ? LazyOptional.of(()->spell).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) storage.writeNBT(SPELL_CASTER_CAP, spell, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            storage.readNBT(SPELL_CASTER_CAP, spell, null, nbt);
        }
    }

}
