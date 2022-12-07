package com.company.prodamgarage;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    public K key;
    public V value;

//    public Pair(Class<K> key, Class<V> value) {
//        this.key = null;
//        this.value = null;
//    }

    public Pair() {
        this.key = null;
        this.value = null;
    }


    public static<K1, V1> Pair<K1, V1> create(K1 key, V1 value) {
//        Pair<K1, V1> ans = new Pair<>((Class<K1>) key.getClass(), (Class<V1>) value.getClass());
        Pair<K1, V1> ans = new Pair<>();

        ans.key = key;
        ans.value = value;

        return ans;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public Pair<K, V> clone() {
        return Pair.create(key, value);
    }
}
