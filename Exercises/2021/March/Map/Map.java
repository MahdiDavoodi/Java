package ir.blog.mahdidavoodi;

public interface Map<K, V> {
    V get(K key);

    V put(K key, V value);

    V remove(K key);

    int size();

    boolean isEmpty();

    Iterable<K> keySet();

    Iterable<V> values();

    Iterable<Entry<K, V>> entrySet();
}
