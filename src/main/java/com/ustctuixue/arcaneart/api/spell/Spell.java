package com.ustctuixue.arcaneart.api.spell;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.ustctuixue.arcaneart.api.spell.effect.*;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
public class Spell
{
    @NonNull
    final List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();

    @NonNull
    final List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

    @NonNull
    final List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();

    @Getter
    private final double drainRate;

    @Getter
    private final double drainSum;

    @Getter @Setter
    private boolean limitedCost;

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder
    {
        @With
        private double drainRate;
        @With
        private double drainSum;
        @With
        boolean limitedCost;

        @NonNull
        final List<ISpellEffectOnHold> effectOnHold = Lists.newArrayList();

        @NonNull
        final List<ISpellEffectOnImpact> effectOnImpact = Lists.newArrayList();

        @NonNull
        final List<ISpellEffectOnRelease> effectOnRelease = Lists.newArrayList();

        public Builder addEffectOnHold(ISpellEffectOnHold effect)
        {
            effectOnHold.add(effect);
        }

    }
}
