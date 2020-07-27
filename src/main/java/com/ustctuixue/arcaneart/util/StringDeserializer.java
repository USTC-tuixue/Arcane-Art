package com.ustctuixue.arcaneart.util;

import javax.annotation.Nullable;

public interface StringDeserializer<T>
{
    @Nullable
    T deserialize(String s);
}
