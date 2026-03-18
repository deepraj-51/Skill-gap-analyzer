package com.skillgap.controller;

public class Map {
    public static <K, V> java.util.Map<K, V> of(K k1, V v1) {
        return java.util.Collections.singletonMap(k1, v1);
    }
}
