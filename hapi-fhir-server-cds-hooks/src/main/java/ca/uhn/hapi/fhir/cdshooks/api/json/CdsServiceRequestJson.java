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
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a CDS Hooks Service Request
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsServiceRequestJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty(value = "hook", required = true)
	String myHook;

	@JsonProperty(value = "hookInstance", required = true)
	String myHookInstance;

	@JsonProperty("fhirServer")
	String myFhirServer;

	@JsonProperty("fhirAuthorization")
	CdsServiceRequestAuthorizationJson myServiceRequestAuthorizationJson;

	@JsonProperty(value = "context", required = true)
	CdsServiceRequestContextJson myContext;

	@JsonProperty("prefetch")
	Map<String, IBaseResource> myPrefetch;

	public String getHookInstance() {
		return myHookInstance;
	}

	public CdsServiceRequestJson setHookInstance(String theHookInstance) {
		myHookInstance = theHookInstance;
		return this;
	}

	public String getFhirServer() {
		return myFhirServer;
	}

	public CdsServiceRequestJson setFhirServer(String theFhirServer) {
		myFhirServer = theFhirServer;
		return this;
	}

	public String getHook() {
		return myHook;
	}

	public CdsServiceRequestJson setHook(String theHook) {
		myHook = theHook;
		return this;
	}

	public CdsServiceRequestContextJson getContext() {
		if (myContext == null) {
			myContext = new CdsServiceRequestContextJson();
		}
		return myContext;
	}

	public CdsServiceRequestJson setContext(CdsServiceRequestContextJson theContext) {
		myContext = theContext;
		return this;
	}

	public CdsServiceRequestAuthorizationJson getServiceRequestAuthorizationJson() {
		if (myServiceRequestAuthorizationJson == null) {
			myServiceRequestAuthorizationJson = new CdsServiceRequestAuthorizationJson();
		}
		return myServiceRequestAuthorizationJson;
	}

	public CdsServiceRequestJson setServiceRequestAuthorizationJson(
			CdsServiceRequestAuthorizationJson theServiceRequestAuthorizationJson) {
		myServiceRequestAuthorizationJson = theServiceRequestAuthorizationJson;
		return this;
	}

	public void addPrefetch(String theKey, IBaseResource theResource) {
		if (myPrefetch == null) {
			myPrefetch = new LinkedHashMap<>();
		}
		myPrefetch.put(theKey, theResource);
	}

	public IBaseResource getPrefetch(String theKey) {
		if (myPrefetch == null) {
			return null;
		}
		return myPrefetch.get(theKey);
	}

	public void addContext(String theKey, Object theValue) {
		getContext().put(theKey, theValue);
	}

	@Nonnull
	public Set<String> getPrefetchKeys() {
		if (myPrefetch == null) {
			return new HashSet<>();
		}
		return Collections.unmodifiableSet(myPrefetch.keySet());
	}
}
