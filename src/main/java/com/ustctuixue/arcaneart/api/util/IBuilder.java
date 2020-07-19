package com.ustctuixue.arcaneart.api.util;

import javax.annotation.Nonnull;

public interface IBuilder<T>
{
    @Nonnull
    T build();
}
