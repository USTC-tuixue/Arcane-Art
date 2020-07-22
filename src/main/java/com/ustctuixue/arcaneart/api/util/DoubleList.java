package com.ustctuixue.arcaneart.api.util;

import java.util.stream.Collectors;

public class DoubleList extends IteratingNonNullList<Double>
{
    public DoubleList numberAdd(Number number)
    {
        DoubleList result = new DoubleList();
        result.addAll(this.stream().map(number1 -> number.doubleValue() + number1).collect(Collectors.toList()));
        return result;
    }

    public DoubleList numberMultiply(Number number)
    {
        DoubleList result = new DoubleList();
        result.addAll(this.stream().map(number1 -> number.doubleValue() * number1).collect(Collectors.toList()));
        return result;
    }
}
