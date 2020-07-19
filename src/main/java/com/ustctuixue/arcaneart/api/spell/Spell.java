package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import com.ustctuixue.arcaneart.api.util.ReflectHelper;
import lombok.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class Spell implements INBTSerializable<ListNBT>
{

    public static final CommandDispatcher<Spell> SPELL_DISPATCHER
            = new CommandDispatcher<>();

    @Getter @NonNull
    ListNBT spell = new ListNBT();

    public Spell(ListNBT nbt)
    {
        this.spell = nbt.copy();
    }

    private final Map<String, Object> variables = Maps.newHashMap();

    @Getter @NonNull
    final List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

    @Getter @NonNull
    final List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();

    @Getter
    double costOnHold;

    @Getter
    double costOnRelease;

    @Getter
    int chargeTick;

    public boolean playerCastOnHold(IManaBar bar, World worldIn, LivingEntity entityLiving, ItemStack stack, int time)
    {

        boolean f = bar.consumeMana(ISpellCostModifier.modifyCost(costOnHold, entityLiving, worldIn));
        if (f)
        {
            effectOnHold.forEach(effect ->
                    effect.onUsingTick(worldIn, entityLiving, stack, time)
            );
        }
        return f;
    }

    public boolean playerCastOnRelease(IManaBar bar, ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {

        boolean f = bar.consumeMana(ISpellCostModifier.modifyCost(costOnRelease, entityLiving, worldIn));
        if (f && 72000 - timeLeft > chargeTick)
        {
            effectOnRelease.forEach(effect ->
                    effect.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft)
            );
        }
        return f;
    }

    public void clear()
    {
        this.effectOnHold.clear();
        this.effectOnRelease.clear();
        this.effectOnImpact.clear();
        this.chargeTick = 0;
        this.costOnHold = 0;
        this.costOnRelease = 0;
    }

    void calculateStat()
    {
        costOnHold = effectOnHold.stream().mapToDouble(ISpellCost::manaCost).sum();
        costOnRelease = effectOnRelease.stream().mapToDouble(ISpellCost::manaCost).sum();
        chargeTick = effectOnRelease.stream().mapToInt(ISpellEffectOnRelease::chargeTick).max().orElse(0);
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
            spell.calculateStat();
        }catch (CommandSyntaxException e)
        {
            spell.clear();
        }

    }

    public Object getVariable(String name)
    {
        return variables.get(name);
    }

    public void setVariable(String name, Object obj)
    {
        variables.put(name, obj);
    }
}
