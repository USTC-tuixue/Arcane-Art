package com.ustctuixue.arcaneart.api.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MinMaxBound<T extends Comparable<T>>
{
    @Nullable
    @Getter
    private final T min;
    @Nullable
    @Getter
    private final T max;

    public boolean validate(T value)
    {
        return (min == null || min.compareTo(value) <= 0) && (max == null || max.compareTo(value) >= 0);
    }

    public static <T extends Comparable<T>> MinMaxBound<T> atLeast(T min)
    {
        return new MinMaxBound<>(min, null);
    }

    public static <T extends Comparable<T>> MinMaxBound<T> atMost(T max)
    {
        return new MinMaxBound<>(null, max);
    }

    public static <T extends Comparable<T>> MinMaxBound<T> of(T min, T max)
    {
        return new MinMaxBound<>(min, max);
    }

    public static <T extends Comparable<T>> MinMaxBound<T> unBounded()
    {
        return new MinMaxBound<>(null, null);
    }
}
