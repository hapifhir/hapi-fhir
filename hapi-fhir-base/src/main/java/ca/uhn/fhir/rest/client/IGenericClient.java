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

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

public interface IGenericClient {

	/**
	 * Implementation of the "instance read" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @return The resource
	 */
	<T extends IResource> T read(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "instance vread" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @param theVersionId The version ID
	 * @return The resource
	 */
	<T extends IResource> T vread(Class<T> theType, IdDt theId, IdDt theVersionId);

	/**
	 * Implementation of the "instance search" method.
	 * @param theType The type of resource to load
	 * @param theParams
	 * @return
	 */
	<T extends IResource> Bundle search(Class<T> theType, Map<String, List<IQueryParameterType>> theParams);

	/**
	 * Implementation of the "instance update" method.
	 * 
	 * @param theId The ID to update
	 * @param theResource The new resource body
	 * @return An outcome containing the results and possibly the new version ID 
	 */
	MethodOutcome update(IdDt theIdDt, IResource theResource);

	/**
	 * Implementation of the "type validate" method.
	 * 
	 * @param theId The ID to validate
	 * @param theResource The resource to validate
	 * @return An outcome containing any validation issues 
	 */
	MethodOutcome validate(IResource theResource);

	/**
	 * Implementation of the "delete instance" method.
	 * @param theType The type of resource to delete
	 * @param theId the ID of the resource to delete
	 * @return An outcome
	 */
	MethodOutcome delete(Class<? extends IResource> theType, IdDt theId);

	/**
	 * Implementation of the "history instance" method.
	 * @param theType The type of resource to return the history for
	 * @param theId the ID of the resource to return the history for
	 * @return An outcome
	 */
	<T extends IResource> Bundle history(Class<T> theType, IdDt theIdDt);

	/**
	 * Retrieves and returns the server conformance statement
	 */
	Conformance conformance();

	/**
	 * Implementation of the "type create" method.
	 * @param theResource The resource to create
	 * @return An outcome
	 */
	MethodOutcome create(IResource theResource);

}
