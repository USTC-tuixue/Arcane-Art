package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import lombok.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

@RequiredArgsConstructor
public class Spell implements INBTSerializable<ListNBT>
{

    public static final CommandDispatcher<Spell> SPELL_DISPATCHER
            = new CommandDispatcher<>();

    @Getter @NonNull
    ListNBT spell;

    @Getter @NonNull
    final List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();

    public void playerCastOnHold(IManaBar bar, World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        for (ISpellEffectOnHold effect:
             effectOnHold)
        {
            if (bar.consumeMana(effect.manaCost()))
            {
                effect.onItemRightClick(worldIn, playerIn, handIn);
            }
        }
    }

    public void playerCastOnRelease(IManaBar bar, ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        for (ISpellEffectOnRelease effect :
                effectOnRelease)
        {
            if (bar.consumeMana(effect.manaCost()))
            {
                effect.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
            }
        }
    }

    public void clear()
    {
        this.effectOnHold.clear();
        this.effectOnRelease.clear();
        this.effectOnImpact.clear();
    }

    @Override
    public ListNBT serializeNBT()
    {
        return spell;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void deserializeNBT(ListNBT nbt)
    {
        if (nbt.getTagType() == 8)
        {
            this.spell = nbt.copy();
            parse((List<String>) ReflectHelper.getListNBTValues(nbt, 8), this);
        }
    }

    public static void parse(List<String> incantations, Spell spell)
    {
        try
        {
            for (String incantation :
                    incantations)
            {
                SPELL_DISPATCHER.execute(incantation, spell);
            }
        }catch (CommandSyntaxException e)
        {
            spell.clear();
        }
    }
}
