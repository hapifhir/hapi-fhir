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

import ca.uhn.fhir.i18n.Msg;

import java.util.Iterator;
import java.util.ServiceLoader;

@SuppressWarnings("unchecked")
public class CacheFactory {

	@SuppressWarnings("rawtypes")
	static ServiceLoader<CacheProvider> loader = ServiceLoader.load(CacheProvider.class);

	@SuppressWarnings("rawtypes")
	private static synchronized <K, V> CacheProvider<K, V> getCacheProvider() {
		Iterator<CacheProvider> iterator = loader.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new RuntimeException(
				Msg.code(2200)
						+ "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}

	public static <K, V> Cache<K, V> build(long theTimeoutMillis) {
		CacheProvider<Object, Object> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis);
	}

	public static <K, V> LoadingCache<K, V> build(long theTimeoutMillis, CacheLoader<K, V> theCacheLoader) {
		CacheProvider<K, V> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theCacheLoader);
	}

	public static <K, V> Cache<K, V> build(long theTimeoutMillis, long theMaximumSize) {
		CacheProvider<Object, Object> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theMaximumSize);
	}

	public static <K, V> LoadingCache<K, V> build(
			long theTimeoutMillis, long theMaximumSize, CacheLoader<K, V> cacheLoader) {
		CacheProvider<K, V> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theMaximumSize, cacheLoader);
	}
}
