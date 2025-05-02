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

import java.util.Optional;

public class AdapterUtils {

	/**
	 * Main entry point for adapter calls.
	 * Implements three conversions: cast to the target type, use IAdaptable if present, or lastly try the AdapterManager.INSTANCE.
	 * @param theObject the object to be adapted
	 * @param theTargetType the type of the adapter requested
	 */
	static <T> Optional<T> adapt(Object theObject, Class<T> theTargetType) {
		if (theTargetType.isInstance(theObject)) {
			//noinspection unchecked
			return Optional.of((T) theObject);
		}

		if (theObject instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) theObject;
			var adapted = adaptable.getAdapter(theTargetType);
			if (adapted.isPresent()) {
				return adapted;
			}
		}

		return AdapterManager.INSTANCE.getAdapter(theObject, theTargetType);
	}
}
