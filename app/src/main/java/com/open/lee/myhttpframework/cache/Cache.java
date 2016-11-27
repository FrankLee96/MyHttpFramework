package com.open.lee.myhttpframework.cache;

/**
 * Created by Lee on 2016/11/27.
 */

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void remove(K key);
}
