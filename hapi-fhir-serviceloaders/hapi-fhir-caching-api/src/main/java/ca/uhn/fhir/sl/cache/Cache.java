package ca.uhn.fhir.sl.cache;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * This interface is a blend between
 * <a href="https://github.com/ben-manes/caffeine">Caffeine's Cache</a> and
 * <a href="https://github.com/google/guava/wiki/CachesExplained"></a>Guava's Cache</a>.
 *
 * Please check their documentation for information in the methods below.
 */
public interface Cache<K, V> {
    V getIfPresent(K key);

    V get(K key, Function<? super K, ? extends V> mappingFunction);

    Map<K, V> getAllPresent(Iterable<? extends K> keys);

    void put(K key, V value);

    void putAll(Map<? extends K, ? extends V> map);

    void invalidate(K key);

    void invalidateAll(Iterable<? extends K> keys);

    void invalidateAll();

    long estimatedSize();

    void cleanUp();
}
