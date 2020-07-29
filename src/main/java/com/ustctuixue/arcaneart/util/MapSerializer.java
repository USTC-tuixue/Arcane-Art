package com.ustctuixue.arcaneart.util;


import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor @NoArgsConstructor
public class MapSerializer<K, V>
{
    @Getter @With
    private StringSerializer<K> keySerializer = K::toString;
    @Getter @With
    private StringSerializer<V> valueSerializer = V::toString;
    @Getter @Nullable @With
    private StringDeserializer<K> keyDeserializer;
    @Getter @Nullable @With
    private StringDeserializer<V> valueDeserializer;

    public List<String> serialize(@Nonnull Map<K, V> map, String delimiter)
    {
        return map.entrySet().stream().map(kvEntry ->
                String.join(delimiter, keySerializer.serialize(kvEntry.getKey()), valueSerializer.serialize(kvEntry.getValue()))
        )
                .collect(Collectors.toList());
    }

    @Nullable
    public Map<K, V> deserialize(@Nonnull List<String> serialized, String regex, boolean requireValueNonNull)
    {
        Map<K, V> result = Maps.newHashMap();
        if (keyDeserializer == null || valueDeserializer == null)
        {
            return null;
        }
        for (String s : serialized)
        {
            String[] divided = s.split(regex, 1);
            K key = keyDeserializer.deserialize(divided[0]);
            if (key != null)
            {
                V value = valueDeserializer.deserialize(divided[1]);
                if (!requireValueNonNull || value != null)
                {
                    result.put(key, value);
                }
            }
        }
        return result;

    }

    public Map<K, V> deserialize(List<String> serialized, String regex)
    {
        return deserialize(serialized, regex, true);
    }
}
