/*
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableCollection;

public class CollectionUtil {

	/**
	 * Non instantiable
	 */
	private CollectionUtil() {
		// nothing
	}

	/**
	 * Returns an immutable union of both collections. If either or both arguments are
	 * <code>null</code> they will be treated as an empty collection, meaning
	 * that even if both arguments are <code>null</code>, an empty immutable
	 * collection will be returned.
	 * <p>
	 * DO NOT use this method if the underlying collections can be changed
	 * after calling this method, as the behaviour is indeterminate.
	 * </p>
	 *
	 * @param theCollection0 The first set in the union, or <code>null</code>.
	 * @param theCollection1 The second set in the union, or <code>null</code>.
	 * @return Returns a union of both collections. Will not return <code>null</code> ever.
	 * @since 7.4.0
	 */
	@Nonnull
	public static <T> Collection<T> nullSafeUnion(
			@Nullable Collection<T> theCollection0, @Nullable Collection<T> theCollection1) {
		Collection<T> collection0 = theCollection0;
		if (collection0 != null && collection0.isEmpty()) {
			collection0 = null;
		}
		Collection<T> collection1 = theCollection1;
		if (collection1 != null && collection1.isEmpty()) {
			collection1 = null;
		}
		if (collection0 == null && collection1 == null) {
			return Collections.emptySet();
		}
		if (collection0 == null) {
			return unmodifiableCollection(collection1);
		}
		if (collection1 == null) {
			return unmodifiableCollection(collection0);
		}
		return CollectionUtils.union(collection0, collection1);
	}

	/**
	 * This method is equivalent to <code>Set.of(...)</code> but is kept here
	 * and used instead of that method because Set.of is not present on Android
	 * SDKs (at least up to 29).
	 * <p>
	 * Sets returned by this method are unmodifiable.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> newSet(T... theValues) {
		HashSet<T> retVal = new HashSet<>();
		Collections.addAll(retVal, theValues);
		return Collections.unmodifiableSet(retVal);
	}
}
