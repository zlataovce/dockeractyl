package me.kcra.dockeractyl.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutablePair<K, V> {
    private final K key;
    private final V value;

    public static <O, T> ImmutablePair<O, T> of(O key, T value) {
        return new ImmutablePair<>(key, value);
    }
}
