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

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for external service that builds adaptors for targets.
 */
public interface IAdapterFactory {
	/**
	 * Build an adaptor for the target.
	 * May return empty() even if the target type is listed in getAdapters() when
	 * the factory fails to convert a particular instance.
	 *
	 * @param theObject the object to be adapted.
	 * @param theAdapterType the target type
	 * @return the adapter, if possible.
	 */
	<T> Optional<T> getAdapter(Object theObject, Class<T> theAdapterType);

	/**
	 * @return the collection of adapter target types handled by this factory.
	 */
	Collection<Class<?>> getAdapters();
}
