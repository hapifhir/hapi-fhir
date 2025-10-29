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
package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Small service class to inject DB access into an interceptor
 * For example, used in bulk export security to allow querying for resource to match against permission argument filters
 */
public interface IAuthResourceResolver {
	IBaseResource resolveCompartmentById(IIdType theResourceId);

	/**
	 * Resolve a list of resources by ID. All resources should be the same type.
	 * @param theResourceIds the FHIR id of the resource(s)
	 * @param theResourceType the type of resource
	 * @return A list of resources resolved by ID
	 */
	List<IBaseResource> resolveCompartmentByIds(List<String> theResourceIds, String theResourceType);
}
