package com.ustctuixue.arcaneart.api.spell;


import com.ustctuixue.arcaneart.api.mp.IManaBar;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import lombok.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.List;

@AllArgsConstructor
public class Spell
{
    public static Marker MARKER = MarkerManager.getMarker("SPELL");

    @Getter
    private String name;

    @Getter
    private List<String> incantations;

    @NonNull
    List<ISpellEffectOnHold> effectOnHold;

    @NonNull
    List<ISpellEffectOnRelease> effectOnRelease;

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

    public void copyFrom(Spell spell)
    {
        this.costOnRelease = spell.costOnRelease;
        this.costOnHold = spell.costOnHold;
        this.chargeTick = spell.chargeTick;

        this.name = spell.name;
        this.incantations = spell.incantations;

        this.effectOnRelease = spell.effectOnRelease;
        this.effectOnHold = spell.effectOnHold;
    }

    public boolean hasEffect()
    {
        return this.effectOnHold.size() != 0 || this.effectOnRelease.size() != 0;
    }
}
