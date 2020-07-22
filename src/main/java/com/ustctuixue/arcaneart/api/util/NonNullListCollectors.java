package com.ustctuixue.arcaneart.api.util;

import net.minecraft.util.NonNullList;

import java.util.stream.Collector;

public class NonNullListCollectors
{

    public static <T> Collector<? super T, ?, ? extends NonNullList<T>> toNonNullList()
    {
        return Collector.of(
                NonNullList::create,
                NonNullList::add,
                (l, r) -> {
                    l.addAll(r);
                    return l;
                }
        );
    }
}
