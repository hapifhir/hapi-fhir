package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IBaseOn<T> {

	/**
	 * Perform the operation across all versions of all resources of all types on the server
	 */
	T onServer();
	
	/**
	 * Perform the operation across all versions of all resources of the given type on the server
	 */
	T onType(Class<? extends IBaseResource> theResourceType);

	/**
	 * Perform the operation across all versions of all resources of the given type on the server
	 *
	 * @param theResourceType The resource type name, e.g. "ValueSet"
	 */
	T onType(String theResourceType);

	/**
	 * Perform the operation across all versions of a specific resource (by ID and type) on the server.
	 * Note that <code>theId</code> must be populated with both a resource type and a resource ID at
	 * a minimum.
	 * 
	 * @throws IllegalArgumentException If <code>theId</code> does not contain at least a resource type and ID 
	 */
	T onInstance(IIdType theId);

	/**
	 * Perform the operation across all versions of a specific resource (by ID and type) on the server.
	 * Note that <code>theId</code> must be populated with both a resource type and a resource ID at
	 * a minimum.
	 *
	 * @throws IllegalArgumentException If <code>theId</code> does not contain at least a resource type and ID
	 */
	T onInstance(String theId);

}
