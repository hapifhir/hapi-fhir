package org.hl7.fhir.instance.model.api;

import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

public interface IAnyResource extends IBaseResource {

	/**
	 * Search parameter constant for <b>_language</b>
	 */
	@SearchParamDefinition(name="_language", path="", description="The language of the resource", type="string"  )
	public static final String SP_RES_LANGUAGE = "_language";


	/**
	 * Search parameter constant for <b>_id</b>
	 */
	@SearchParamDefinition(name="_id", path="", description="The ID of the resource", type="token"  )
	public static final String SP_RES_ID = "_id";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_id</b>
	 * <p>
	 * Description: <b>the _id of a resource</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Resource._id</b><br>
	 * </p>
	 */
	public static final TokenClientParam RES_ID = new TokenClientParam(IAnyResource.SP_RES_ID);

	String getId();

	@Override
	IIdType getIdElement();

	IPrimitiveType<String> getLanguageElement();

	public Object getUserData(String name);

	@Override
	IAnyResource setId(String theId);

	public void setUserData(String name, Object value);

}
