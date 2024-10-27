/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.jpa;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JpaSectionSearchStrategyCollection {

	private Map<Class<? extends IBaseResource>, Object> mySearchStrategies;

	private JpaSectionSearchStrategyCollection(Map<Class<? extends IBaseResource>, Object> theSearchStrategies) {
		mySearchStrategies = theSearchStrategies;
	}

	@SuppressWarnings("unchecked")
	public <T extends IBaseResource> IJpaSectionSearchStrategy<T> getSearchStrategy(Class<T> theClass) {
		return (IJpaSectionSearchStrategy<T>) mySearchStrategies.get(theClass);
	}

	public Collection<Class<? extends IBaseResource>> getResourceTypes() {
		return mySearchStrategies.keySet();
	}

	public static JpaSectionSearchStrategyCollectionBuilder newBuilder() {
		return new JpaSectionSearchStrategyCollectionBuilder();
	}

	public static class JpaSectionSearchStrategyCollectionBuilder {
		private Map<Class<? extends IBaseResource>, Object> mySearchStrategies = new HashMap<>();

		public <T extends IBaseResource> JpaSectionSearchStrategyCollectionBuilder addStrategy(
				Class<T> theType, IJpaSectionSearchStrategy<T> theSearchStrategy) {
			mySearchStrategies.put(theType, theSearchStrategy);
			return this;
		}

		public JpaSectionSearchStrategyCollection build() {
			return new JpaSectionSearchStrategyCollection(mySearchStrategies);
		}
	}
}
