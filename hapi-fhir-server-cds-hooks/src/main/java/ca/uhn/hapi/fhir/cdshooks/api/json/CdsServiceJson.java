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
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a CDS Hooks Service descriptor
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsServiceJson extends BaseCdsServiceJson implements IModelJson {
	public static final String HOOK = "hook";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PREFETCH = "prefetch";

	@JsonProperty(value = HOOK, required = true)
	String myHook;

	@JsonProperty(value = TITLE)
	String myTitle;

	@JsonProperty(value = DESCRIPTION, required = true)
	String myDescription;

	@JsonProperty(value = ID, required = true)
	String myId;

	@JsonProperty(PREFETCH)
	private Map<String, String> myPrefetch;

	private Map<String, CdsResolutionStrategyEnum> mySource;

	public String getHook() {
		return myHook;
	}

	public CdsServiceJson setHook(String theHook) {
		myHook = theHook;
		return this;
	}

	public String getTitle() {
		return myTitle;
	}

	public CdsServiceJson setTitle(String theTitle) {
		myTitle = theTitle;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public CdsServiceJson setDescription(String theDescription) {
		myDescription = theDescription;
		return this;
	}

	public String getId() {
		return myId;
	}

	public CdsServiceJson setId(String theId) {
		myId = theId;
		return this;
	}

	public void addPrefetch(String theKey, String theQuery) {
		if (myPrefetch == null) {
			myPrefetch = new LinkedHashMap<>();
		}
		myPrefetch.put(theKey, theQuery);
	}

	public Map<String, String> getPrefetch() {
		if (myPrefetch == null) {
			myPrefetch = new LinkedHashMap<>();
		}
		return Collections.unmodifiableMap(myPrefetch);
	}

	public void addSource(String theKey, CdsResolutionStrategyEnum theSource) {
		if (mySource == null) {
			mySource = new LinkedHashMap<>();
		}
		mySource.put(theKey, theSource);
	}

	public Map<String, CdsResolutionStrategyEnum> getSource() {
		if (mySource == null) {
			mySource = new LinkedHashMap<>();
		}
		return Collections.unmodifiableMap(mySource);
	}
}
