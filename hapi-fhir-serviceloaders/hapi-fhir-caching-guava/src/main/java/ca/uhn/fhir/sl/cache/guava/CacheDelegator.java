package ca.uhn.fhir.sl.cache.guava;

/*-
 * #%L
 * HAPI FHIR - ServiceLoaders - Caching Guava
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

import ca.uhn.fhir.i18n.Msg;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class CacheDelegator<K, V> implements ca.uhn.fhir.sl.cache.Cache<K, V> {

	com.google.common.cache.Cache<K, V> cache;

	public CacheDelegator(com.google.common.cache.Cache<K, V> impl) {
		this.cache = impl;
	}

	@Override
	public V getIfPresent(K key) {
		return cache.getIfPresent(key);
	}

	@Override
	public V get(K key, Function<? super K, ? extends V> mappingFunction) {
		try {
			return cache.get(key, () -> mappingFunction.apply(key));
		} catch (ExecutionException e) {
			throw new RuntimeException(Msg.code(2206) + "Failed to red from cache", e);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException) e.getCause();
			}
			throw e;
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception
			// This matches the behaviour of Caffeine
			return null;
		}
	}

	@Override
	public Map<K, V> getAllPresent(Iterable<? extends K> keys) {
		return cache.getAllPresent(keys);
	}

	@Override
	public void put(K key, V value) {
		cache.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		cache.putAll(map);
	}

	@Override
	public void invalidate(K key) {
		cache.invalidate(key);
	}

	@Override
	public void invalidateAll(Iterable<? extends K> keys) {
		cache.invalidateAll(keys);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}

	@Override
	public long estimatedSize() {
		return cache.size();
	}

	@Override
	public void cleanUp() {
		cache.cleanUp();
	}
}
