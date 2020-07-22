package com.ustctuixue.arcaneart.api.util;

public interface IIteratingList<T>
{
    T next();
    void resetCursor();
    void setCursor(int cursorIn);
    int getCursor();
}
