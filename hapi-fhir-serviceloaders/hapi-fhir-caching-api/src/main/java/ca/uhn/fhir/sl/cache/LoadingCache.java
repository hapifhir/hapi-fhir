package ca.uhn.fhir.sl.cache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This interface is a blend between
 * <a href="https://github.com/ben-manes/caffeine">Caffeine's LoadingCache</a> and
 * <a href="https://github.com/google/guava/wiki/CachesExplained"></a>Guava's LoadingCache</a>.
 *
 * Please check their documentation for information in the methods below.
 */
public interface LoadingCache<K extends Object, V extends Object> extends Cache<K, V> {
    V get(K key);

    Map<K, V> getAll(Iterable<? extends K> keys);

    void refresh(K key);
}
