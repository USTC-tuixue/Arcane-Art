package com.ustctuixue.arcaneart.api.spell.interpreter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SpellSideEffect
{
    private double mpCost = 0;
    private double complexity = 0;

    public SpellSideEffect set(double mpCostIn, double complexityIn)
    {
        this.mpCost = mpCostIn;
        this.complexity = complexityIn;
        return this;
    }

    public SpellSideEffect set(SpellSideEffect effect)
    {
        this.mpCost = effect.mpCost;
        this.complexity = effect.complexity;
        return this;
    }

    public SpellSideEffect add(SpellSideEffect effect)
    {
        this.mpCost += effect.mpCost;
        this.complexity += effect.complexity;
        return this;
    }

    public SpellSideEffect operateMpCost(Function<Double, Double> operation)
    {
        this.mpCost = operation.apply(this.mpCost);
        return this;
    }

    public SpellSideEffect operateComplexity(Function<Double, Double> operation)
    {
        this.complexity = operation.apply(this.complexity);
        return this;
    }

    public SpellSideEffect mulMpCost(double factor)
    {
        return operateMpCost(d->d*factor);
    }

}
