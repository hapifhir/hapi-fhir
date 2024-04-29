/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.esr;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IExternallyStoredResourceService {

	/**
	 * Returns the ID of this provider. No two providers may return the same
	 * ID, and this provider should always return the same ID.
	 */
	String getId();

	/**
	 * Fetches the given resource using the given address string
	 *
	 * @param theAddress The address string is a format that is entirely up to the individual provider. HAPI FHIR
	 *                   doesn't try to understand it.
	 * @return HAPI FHIR may modify the returned object, so it is important to always return a new object for every call here (careful with caching!)
	 */
	IBaseResource fetchResource(String theAddress);
}
