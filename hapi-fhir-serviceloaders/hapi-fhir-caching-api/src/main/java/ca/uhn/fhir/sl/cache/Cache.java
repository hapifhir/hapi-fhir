package ca.uhn.fhir.sl.cache;

/*-
 * #%L
 * HAPI FHIR - ServiceLoaders - Caching API
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;
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
