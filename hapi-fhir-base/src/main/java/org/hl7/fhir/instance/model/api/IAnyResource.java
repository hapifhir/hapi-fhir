package org.hl7.fhir.instance.model.api;

import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

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

/**
 * An IBaseResource that has a FHIR version of DSTU3 or higher
 */
public interface IAnyResource extends IBaseResource {

	/**
	 * Search parameter constant for <b>_id</b>
	 */
	@SearchParamDefinition(name="_id", path="", description="The ID of the resource", type="token")
	String SP_RES_ID = "_id";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_id</b>
	 * <p>
	 * Description: <b>the _id of a resource</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Resource._id</b><br>
	 * </p>
	 */
	TokenClientParam RES_ID = new TokenClientParam(IAnyResource.SP_RES_ID);

	String getId();

	IIdType getIdElement();

	IPrimitiveType<String> getLanguageElement();

	Object getUserData(String name);

	IAnyResource setId(String theId);

	void setUserData(String name, Object value);

}
