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

import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheLoader;
import ca.uhn.fhir.sl.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheProvider<K, V> implements ca.uhn.fhir.sl.cache.CacheProvider<K, V> {

	public Cache<K, V> create(long timeoutMillis) {
		return new CacheDelegator<K, V>(CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build());
	}

	public LoadingCache<K, V> create(long timeoutMillis, CacheLoader<K, V> loading) {
		return new LoadingCacheDelegator<K, V>(CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build(new com.google.common.cache.CacheLoader<>() {
					@Override
					public V load(K k) throws Exception {
						return loading.load(k);
					}
				}));
	}

	public Cache<K, V> create(long timeoutMillis, long maximumSize) {
		return new CacheDelegator<K, V>(CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build());
	}

	public LoadingCache<K, V> create(long timeoutMillis, long maximumSize, CacheLoader<K, V> loading) {
		return new LoadingCacheDelegator<K, V>(CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build(new com.google.common.cache.CacheLoader<>() {
					@Override
					public V load(K k) throws Exception {
						return loading.load(k);
					}
				}));
	}
}
