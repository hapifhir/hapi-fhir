package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;

public interface IGenericClient {

	/**
	 * Implementation of the "read" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @return The resource
	 */
	<T extends IResource> T read(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "vread" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @param theVersionId The version ID
	 * @return The resource
	 */
	<T extends IResource> T vread(Class<T> theType, IdDt theId, IdDt theVersionId);

}
