/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Small service class to inject DB access into an interceptor. Some examples include:
 * <ul>
 *     <li>bulk export security to allow querying for resource to match against permission argument filters</li>
 *     <li>instance $meta operations where only the instance id is known at the time of the request</li>
 * </ul>
 */
public interface IAuthResourceResolver {

	/**
	 * Resolve a resource by ID.
	 * @param theResourceId - The FHIR id of the resource
	 * @param theRequestDetails - The request details
	 * @return A resource resolved by ID
	 */
	@Nullable
	IBaseResource resolveResourceById(IIdType theResourceId, RequestDetails theRequestDetails);

	/**
	 * Resolve a list of resources by ID. All resources should be the same type.
	 * @param theResourceIds the FHIR id of the resource(s)
	 * @param theResourceType the type of resource
	 * @return A list of resources resolved by ID
	 */
	List<IBaseResource> resolveResourcesByIds(List<String> theResourceIds, String theResourceType);
}
