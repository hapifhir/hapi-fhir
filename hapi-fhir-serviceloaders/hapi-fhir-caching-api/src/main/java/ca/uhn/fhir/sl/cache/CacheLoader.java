package ca.uhn.fhir.sl.cache;

public interface CacheLoader<K, V> {
    V load(K var1) throws Exception;
}
