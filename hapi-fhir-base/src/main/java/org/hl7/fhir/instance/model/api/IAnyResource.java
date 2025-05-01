/*
 * #%L
 * HAPI FHIR - Core Library
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
package org.hl7.fhir.instance.model.api;

import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.gclient.UriClientParam;

/**
 * An IBaseResource that has a FHIR version of DSTU3 or higher
 */
public interface IAnyResource extends IBaseResource {

	String SP_RES_ID = "_id";
	/**
	 * Search parameter constant for <b>_id</b>
	 */
	@SearchParamDefinition(
			name = SP_RES_ID,
			path = "Resource.id",
			description = "The ID of the resource",
			type = "token")

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_id</b>
	 * <p>
	 * Description: <b>the _id of a resource</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Resource.id</b><br>
	 * </p>
	 */
	TokenClientParam RES_ID = new TokenClientParam(IAnyResource.SP_RES_ID);

	String SP_RES_LAST_UPDATED = "_lastUpdated";
	/**
	 * Search parameter constant for <b>_lastUpdated</b>
	 */
	@SearchParamDefinition(
			name = SP_RES_LAST_UPDATED,
			path = "Resource.meta.lastUpdated",
			description = "Only return resources which were last updated as specified by the given range",
			type = "date")

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_lastUpdated</b>
	 * <p>
	 * Description: <b>The last updated date of a resource</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>Resource.meta.lastUpdated</b><br>
	 * </p>
	 */
	DateClientParam RES_LAST_UPDATED = new DateClientParam(IAnyResource.SP_RES_LAST_UPDATED);

	String SP_RES_TAG = "_tag";
	/**
	 * Search parameter constant for <b>_tag</b>
	 */
	@SearchParamDefinition(
			name = SP_RES_TAG,
			path = "Resource.meta.tag",
			description = "The tag of the resource",
			type = "token")

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_tag</b>
	 * <p>
	 * Description: <b>The tag of a resource</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Resource.meta.tag</b><br>
	 * </p>
	 */
	TokenClientParam RES_TAG = new TokenClientParam(IAnyResource.SP_RES_TAG);

	String SP_RES_PROFILE = "_profile";
	/**
	 * Search parameter constant for <b>_profile</b>
	 */
	@SearchParamDefinition(
			name = SP_RES_PROFILE,
			path = "Resource.meta.profile",
			description = "The profile of the resource",
			type = "uri")

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_profile</b>
	 * <p>
	 * Description: <b>The profile of a resource</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>Resource.meta.profile</b><br>
	 * </p>
	 */
	UriClientParam RES_PROFILE = new UriClientParam(IAnyResource.SP_RES_PROFILE);

	String SP_RES_SECURITY = "_security";
	/**
	 * Search parameter constant for <b>_security</b>
	 */
	@SearchParamDefinition(
			name = SP_RES_SECURITY,
			path = "Resource.meta.security",
			description = "The security of the resource",
			type = "token")

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_security</b>
	 * <p>
	 * Description: <b>The security of a resource</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Resource.meta.security</b><br>
	 * </p>
	 */
	TokenClientParam RES_SECURITY = new TokenClientParam(IAnyResource.SP_RES_SECURITY);

	String getId();

	IIdType getIdElement();

	IPrimitiveType<String> getLanguageElement();

	Object getUserData(String name);

	IAnyResource setId(String theId);

	void setUserData(String name, Object value);
}
