/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A structure holding an OAuth 2.0 bearer access token granting the CDS Service access to FHIR resource
 */
public class CdsServiceRequestAuthorizationJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty(value = "access_token", required = true)
	String myAccessToken;

	@JsonProperty(value = "token_type", required = true)
	String myTokenType;

	@JsonProperty(value = "expires_in", required = true)
	Long myExpiresIn;

	@JsonProperty(value = "scope", required = true)
	String myScope;

	@JsonProperty(value = "subject", required = true)
	String mySubject;

	public String getAccessToken() {
		return myAccessToken;
	}

	public CdsServiceRequestAuthorizationJson setAccessToken(String theAccessToken) {
		myAccessToken = theAccessToken;
		return this;
	}

	public String getTokenType() {
		return myTokenType;
	}

	public CdsServiceRequestAuthorizationJson setTokenType(String theTokenType) {
		myTokenType = theTokenType;
		return this;
	}

	public Long getExpiresIn() {
		return myExpiresIn;
	}

	public CdsServiceRequestAuthorizationJson setExpiresIn(Long theExpiresIn) {
		myExpiresIn = theExpiresIn;
		return this;
	}

	public String getScope() {
		return myScope;
	}

	public CdsServiceRequestAuthorizationJson setScope(String theScope) {
		myScope = theScope;
		return this;
	}

	public String getSubject() {
		return mySubject;
	}

	public CdsServiceRequestAuthorizationJson setSubject(String theSubject) {
		mySubject = theSubject;
		return this;
	}
}
