package org.hl7.fhir.cache;

public interface CacheLoader<K, V> {
    V load(K var1) throws Exception;
}
