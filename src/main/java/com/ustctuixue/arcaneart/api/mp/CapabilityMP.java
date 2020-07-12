package com.ustctuixue.arcaneart.api.mp;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class CapabilityMP
{

    public static IAttribute MAX_MANA = new RangedAttribute(null, "arcaneart.maxMana",
            100.0D, 0.0D, 5000.0D);
    public static IAttribute REGEN_RATE = new RangedAttribute(null, "arcaneart.regenRate",
            4.0D, 0.0D, 1000.0D);


    public static class Storage implements Capability.IStorage<IManaBar>
    {
        static final String MANA = "mana";
        static final String EXP = "experience";
        static final String LVL = "level";
        static final String REGEN = "regenCoolDown";
        /**
         * Serialize the capability instance to a NBTTag.
         * This allows for a central implementation of saving the data.
         * <p>
         * It is important to note that it is up to the API defining
         * the capability what requirements the 'instance' value must have.
         * <p>
         * Due to the possibility of manipulating internal data, some
         * implementations MAY require that the 'instance' be an instance
         * of the 'default' implementation.
         * <p>
         * Review the API docs for more info.
         *
         * @param capability The Capability being stored.
         * @param instance   An instance of that capabilities interface.
         * @param side       The side of the object the instance is associated with.
         * @return a NBT holding the data. Null if no data needs to be stored.
         */
        @Nullable
        @Override
        public INBT writeNBT(Capability<IManaBar> capability, IManaBar instance, Direction side)
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putDouble(MANA, instance.getMana());
            nbt.putDouble(EXP, instance.getMagicExperience());
            nbt.putDouble(REGEN, instance.getRegenCoolDown());
            nbt.putInt(LVL, instance.getMagicLevel());
            return nbt;
        }

        /**
         * Read the capability instance from a NBT tag.
         * <p>
         * This allows for a central implementation of saving the data.
         * <p>
         * It is important to note that it is up to the API defining
         * the capability what requirements the 'instance' value must have.
         * <p>
         * Due to the possibility of manipulating internal data, some
         * implementations MAY require that the 'instance' be an instance
         * of the 'default' implementation.
         * <p>
         * Review the API docs for more info.         *
         *
         * @param capability The Capability being stored.
         * @param instance   An instance of that capabilities interface.
         * @param side       The side of the object the instance is associated with.
         * @param nbt        A NBT holding the data. Must not be null, as doesn't make sense to call this function with nothing to read...
         */
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

}
