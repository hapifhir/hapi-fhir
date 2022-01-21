package ca.uhn.fhir.validation;

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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IResourceLoader {

	/**
	 * Load the latest version of a given resource
	 * 
	 * @param theType
	 *           The type of the resource to load
	 * @param theId
	 *           The ID of the resource to load
	 * @throws ResourceNotFoundException
	 *            If the resource is not known
	 */
	public <T extends IBaseResource> T load(Class<T> theType, IIdType theId) throws ResourceNotFoundException;

}
