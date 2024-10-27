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
import ca.uhn.fhir.sl.cache.LoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoadingCacheDelegator<K, V> extends CacheDelegator<K, V> implements LoadingCache<K, V> {

	public LoadingCacheDelegator(com.google.common.cache.LoadingCache<K, V> impl) {
		super(impl);
	}

	public com.google.common.cache.LoadingCache<K, V> getCache() {
		return (com.google.common.cache.LoadingCache<K, V>) cache;
	}

	@Override
	public V get(K key) {
		try {
			return getCache().get(key);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException) e.getCause();
			}
			throw e;
		} catch (ExecutionException e) {
			throw new RuntimeException(Msg.code(2204) + "Failed to red from cache", e);
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception.
			// This matches the behaviour of Caffeine
			return null;
		}
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		try {
			return getCache().getAll(keys);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException) e.getCause();
			}
			throw e;
		} catch (ExecutionException e) {
			throw new RuntimeException(Msg.code(2205) + "Failed to red from cache", e);
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception
			// This matches the behaviour of Caffeine
			return null;
		}
	}

	@Override
	public void refresh(K key) {
		getCache().refresh(key);
	}
}
