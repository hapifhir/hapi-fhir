/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util.adapters;

import jakarta.annotation.Nonnull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class AdapterManager implements IAdapterManager {
	public static final AdapterManager INSTANCE = new AdapterManager();

	Set<IAdapterFactory> myAdapterFactories = new HashSet<>();

	/**
	 * Hidden to force shared use of the public INSTANCE.
	 */
	AdapterManager() {}

	public <T> @Nonnull Optional<T> getAdapter(Object theObject, Class<T> theTargetType) {
		// todo this can be sped up with a cache of type->Factory.
		return myAdapterFactories.stream()
				.filter(nextFactory -> nextFactory.getAdapters().stream().anyMatch(theTargetType::isAssignableFrom))
				.flatMap(nextFactory -> {
					var adapter = nextFactory.getAdapter(theObject, theTargetType);
					// can't use Optional.stream() because of our Android target is API level 26/JDK 8.
					if (adapter.isPresent()) {
						return Stream.of(adapter.get());
					} else {
						return Stream.empty();
					}
				})
				.findFirst();
	}

	public void registerFactory(@Nonnull IAdapterFactory theFactory) {
		myAdapterFactories.add(theFactory);
	}

	public void unregisterFactory(@Nonnull IAdapterFactory theFactory) {
		myAdapterFactories.remove(theFactory);
	}
}
