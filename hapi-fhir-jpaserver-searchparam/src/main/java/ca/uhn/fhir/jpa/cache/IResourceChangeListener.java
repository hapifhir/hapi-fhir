package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

/**
 * To be notified of resource changes in the repository, implement this interface and register your instance with
 * {@link IResourceChangeListenerRegistry}.
 */
public interface IResourceChangeListener {
	/**
	 * This method is called within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS} of a listener registration
	 * @param theResourceIds the ids of all resources that match the search parameters the listener was registered with
	 */
	void handleInit(Collection<IIdType> theResourceIds);

	/**
	 * Called by the {@link IResourceChangeListenerRegistry} when matching resource changes are detected
	 */
	void handleChange(IResourceChangeEvent theResourceChangeEvent);
}
