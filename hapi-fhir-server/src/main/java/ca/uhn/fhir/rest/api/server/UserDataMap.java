/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RequestDetails.getUserData() used a HashMap previously but that is not thread-safe.  It's not
 * possible to simply replace HashMap with ConcurrentHashMap in RequestDetails since null
 * values work differently. This class is a thin wrapper around ConcurrentHashMap that
 * preserves the expected null value behaviour on get. Note that contains() and size() do
 * behave differently. See UserDataMapTest for details.  We could use Collections.synchronizedMap to achieve
 * the required thread-safety, but ConcurrentHashMap has better performance characteristics.
 */
public class UserDataMap implements Map<Object, Object> {
	private final Map<Object, Object> myMap = new ConcurrentHashMap<>();

	@Override
	public int size() {
		return myMap.size();
	}

	@Override
	public boolean isEmpty() {
		return myMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return myMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		if (value == null) {
			return false;
		}
		return myMap.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return myMap.get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		if (value == null) {
			return myMap.remove(key);
		}
		return myMap.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return myMap.remove(key);
	}

	@Override
	public void clear() {
		myMap.clear();
	}

	@Override
	public void putAll(@Nonnull Map<?, ?> m) {
		// We can't delegate directly to putAll since null keys would throw an exception
		for (Entry<?, ?> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	@Nonnull
	public Set<Object> keySet() {
		return myMap.keySet();
	}

	@Override
	@Nonnull
	public Collection<Object> values() {
		return myMap.values();
	}

	@Override
	@Nonnull
	public Set<Entry<Object, Object>> entrySet() {
		return myMap.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Map)) return false;
		return myMap.equals(o);
	}

	@Override
	public int hashCode() {
		return myMap.hashCode();
	}
}
