package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ustctuixue.arcaneart.api.spell.effect.ISpellCost;
import com.ustctuixue.arcaneart.api.spell.effect.ISpellEffectOnHold;
import com.ustctuixue.arcaneart.api.spell.effect.ISpellEffectOnImpact;
import com.ustctuixue.arcaneart.api.spell.effect.ISpellEffectOnRelease;
import com.ustctuixue.arcaneart.api.util.IBuilder;
import lombok.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SpellBuilder implements IBuilder<Spell>
{
    public static final CommandDispatcher<SpellBuilder> SPELL_DISPATCHER
            = new CommandDispatcher<>();

    private final Map<String, Object> variables = Maps.newHashMap();

    @Getter
    private List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();
    @Getter
    private List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();
    @Getter
    private List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

    @Getter @With
    private String name;
    @Getter @With
    private List<String> incantations;

    private double costOnHold, costOnRelease;
    private int chargeTick;

    void calculateStat()
    {
        costOnHold = effectOnHold.stream().mapToDouble(ISpellCost::manaCost).sum();
        costOnRelease = effectOnRelease.stream().mapToDouble(ISpellCost::manaCost).sum();
        chargeTick = effectOnRelease.stream().mapToInt(ISpellEffectOnRelease::chargeTick).max().orElse(0);
    }

    @Override @Nonnull
    public Spell build()
    {
         return new Spell(
                 name,
                 effectOnHold, effectOnImpact, effectOnRelease,
                 costOnHold, costOnRelease, chargeTick
         );
    }

    public void clearEffects()
    {
        this.effectOnHold.clear();
        this.effectOnImpact.clear();
        this.effectOnRelease.clear();
        this.chargeTick = 0;
        this.costOnHold = 0;
        this.costOnRelease = 0;
    }

    void parse()
    {
        try
        {
            for (String incantation : this.incantations)
            {
                SPELL_DISPATCHER.execute(incantation, this);
            }
            this.calculateStat();
        }catch (CommandSyntaxException e)
        {
            this.clearEffects();
        }
    }

    /**
     *
     * @param name name of variable
     * @param <T> variable type
     * @return variable value if exists and type-consistent, or else null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getVariable(String name, Class<? extends T> clazz)
    {
        Object v = this.variables.get(name);
        return clazz.isInstance(v) ? (T) v : null;
    }

    public void setVariable(String name, Object obj)
    {
        variables.put(name, obj);
    }
}
