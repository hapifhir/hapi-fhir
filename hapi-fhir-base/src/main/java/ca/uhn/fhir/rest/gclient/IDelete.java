package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.api.IResource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

public interface IDelete {

	IDeleteTyped resource(IResource theResource);
	
	IDeleteTyped resourceById(IIdType theId);
	
	IDeleteTyped resourceById(String theResourceType, String theLogicalId);

	/**
	 * Specifies that the delete should be performed as a conditional delete
	 * against a given search URL.
	 *
	 * @param theSearchUrl The search URL to use. The format of this URL should be of the form <code>[ResourceType]?[Parameters]</code>,
	 *                     for example: <code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IDeleteTyped resourceConditionalByUrl(String theSearchUrl);

	/**
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IDeleteWithQuery resourceConditionalByType(String theResourceType);

}
