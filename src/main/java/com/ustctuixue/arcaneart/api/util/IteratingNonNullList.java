package com.ustctuixue.arcaneart.api.util;

import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class IteratingNonNullList<T> extends NonNullList<T> implements IIteratingList<T>
{
    private int cursor = 0;

    @Override
    public int getCursor()
    {
        return cursor;
    }

    @Nonnull
    public T next()
    {
        return this.get(cursor++);
    }

    @Override
    public void resetCursor()
    {
        cursor = 0;
    }

    @Override
    public void setCursor(int cursorIn)
    {
        cursor = cursorIn;
    }
}
