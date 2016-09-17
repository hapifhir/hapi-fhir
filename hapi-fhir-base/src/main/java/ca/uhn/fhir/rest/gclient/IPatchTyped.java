package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import ca.uhn.fhir.rest.api.PatchTypeEnum;

public interface IPatchTyped extends IPatchExecutable {

	IPatchExecutable withId(IIdType theId);

	IPatchExecutable withId(String theId);

	/**
	 * Specifies that the update should be performed as a conditional create
	 * against a given search URL.
	 *
	 * @param theSearchUrl The search URL to use. The format of this URL should be of the form <code>[ResourceType]?[Parameters]</code>,
	 *                     for example: <code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IPatchTyped conditionalByUrl(String theSearchUrl);

	/**
	 * @since HAPI 0.9 / FHIR DSTU 2
	 */
	IPatchWithQuery conditional();
	
	/**
	 * Specifies the format of the patch (either XML or JSON)
	 * @param patchType 
	 */
	IPatchTyped patchType(PatchTypeEnum patchType);
	
	/**
	 * The body of the patch document serialized in either XML or JSON which conforms to
	 * http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261
	 * @param patchBody
	 */
	IPatchTyped patchBody(String patchBody);

}
