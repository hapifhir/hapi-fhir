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
