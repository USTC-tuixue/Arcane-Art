package com.ustctuixue.arcaneart.api.util;

import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectHelper<T>
{

    public static Iterator<INBT> getIterator(ListNBT nbt)
    {
        return Objects.requireNonNull(getList(nbt)).iterator();
    }

    @SuppressWarnings("unchecked")
    public static List<INBT> getList(ListNBT nbt)
    {
        try
        {
            // Must success
            Field tagList = ListNBT.class.getDeclaredField("tagList");
            tagList.setAccessible(true);
            // Must success
            return ((List<INBT>)tagList.get(nbt));
        }catch (NoSuchFieldException | IllegalAccessException ignored)
        {
        }
        return null;
    }


    /**
     * 支持将内部存有基础数据类型的 ListNBT 转为 List
     * @param nbt
     * @return List
     */
    public static List<?> getListNBTValues(ListNBT nbt, int tagType)
    {
        List<? extends INBT> list = Objects.requireNonNull(getList(nbt));
        Stream<? extends INBT> stream = list.stream();
        switch (tagType)
        {
            case 1:
                return stream.map((tag) ->
                        tag != null ? ((ByteNBT) tag).getByte() : 0).collect(Collectors.toList());
            case 2:
                return stream.map((tag) ->
                        tag != null? ((ShortNBT) tag).getShort() : 0).collect(Collectors.toList());
            case 3:
                return stream.map((tag) ->
                        tag != null? ((IntNBT) tag).getInt() : 0).collect(Collectors.toList());
            case 4:
                return stream.map((tag) ->
                        tag != null? ((LongNBT) tag).getLong() : 0).collect(Collectors.toList());
            case 5:
                return stream.map((tag) ->
                        tag != null? ((FloatNBT) tag).getFloat() : 0).collect(Collectors.toList());
            case 6:
                return stream.map((tag) ->
                        tag != null? ((DoubleNBT) tag).getDouble() : 0).collect(Collectors.toList());
            case 8:
                return stream.map((tag)->
                        tag != null? tag.getString() : "").collect(Collectors.toList());
        }
        return list;
    }

    public static List<?> getListNBTValues(ListNBT nbt)
    {
        return getListNBTValues(nbt, nbt.getTagType());
    }
}
