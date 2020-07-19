package com.ustctuixue.arcaneart.api.mp;

import com.ustctuixue.arcaneart.api.InnerNumberDefaults;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class CapabilityMP
{

    public static IAttribute MAX_MANA = new RangedAttribute(null, "arcaneart.maxMana",
            100.0D, 0.0D, InnerNumberDefaults.MAX_ALLOWED_MP);
    public static IAttribute REGEN_RATE = new RangedAttribute(null, "arcaneart.regenRate",
            InnerNumberDefaults.REGEN_RATE, 0.0D, 1.0D);
    @CapabilityInject(IManaBar.class)
    public static Capability<IManaBar> MANA_BAR_CAP;


    public static class Storage implements Capability.IStorage<IManaBar>
    {
        static final String MANA = "mana";
        static final String EXP = "experience";
        static final String LVL = "level";
        static final String REGEN = "regenCoolDown";

        @Nullable
        @Override
        public INBT writeNBT(Capability<IManaBar> capability, IManaBar instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putDouble(MANA, instance.getMana());
            nbt.putDouble(EXP, instance.getMagicExperience());
            nbt.putInt(REGEN, instance.getRegenCoolDown());
            nbt.putInt(LVL, instance.getMagicLevel());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IManaBar> capability, IManaBar instance, Direction side, INBT nbt)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            instance.setMagicExperience(compoundNBT.getDouble(EXP));
            instance.setMagicLevel(compoundNBT.getInt(LVL));
            instance.setMana(compoundNBT.getDouble(MANA));
            instance.setRegenCoolDown(compoundNBT.getInt(REGEN));
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        private final IManaBar manaBar = new DefaultManaBar();
        private final Capability.IStorage<IManaBar> storage = new CapabilityMP.Storage();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            if (cap == CapabilityMP.MANA_BAR_CAP)
            {
                return LazyOptional.of(()->manaBar).cast();
            }
            else
            {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) storage.writeNBT(MANA_BAR_CAP, manaBar, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            storage.readNBT(MANA_BAR_CAP, manaBar, null, nbt);
        }
    }

}
