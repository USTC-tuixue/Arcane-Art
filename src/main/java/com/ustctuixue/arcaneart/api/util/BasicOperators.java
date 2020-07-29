package com.ustctuixue.arcaneart.api.util;

import java.util.function.Function;

public class BasicOperators
{
    public static Function<Double, Double> addBy(double x)
    {
        return y -> x + y;
    }

    public static Function<Double, Double> mulBy(double x)
    {
        return y -> x * y;
    }

    public static Function<Double, Double> subBy(double x)
    {
        return y -> y - x;
    }

    public static Function<Double, Double> divideBy(double x)
    {
        return y -> y / x;
    }
}
