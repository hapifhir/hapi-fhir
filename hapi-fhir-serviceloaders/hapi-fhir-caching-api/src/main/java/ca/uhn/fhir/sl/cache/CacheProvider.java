package ca.uhn.fhir.sl.cache;

/*-
 * #%L
 * HAPI FHIR - ServiceLoaders - Caching API
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

public interface CacheProvider<K, V> {
	Cache create(long timeoutMillis);

	Cache create(long timeoutMillis, long maximumSize);

	LoadingCache create(long timeoutMillis, CacheLoader<K, V> cacheLoader);

	LoadingCache create(long timeoutMillis, long maximumSize, CacheLoader<K, V> cacheLoader);
}
