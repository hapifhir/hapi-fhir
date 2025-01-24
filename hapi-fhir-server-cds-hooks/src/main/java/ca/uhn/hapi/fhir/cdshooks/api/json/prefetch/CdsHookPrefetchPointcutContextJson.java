/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.api.json.prefetch;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains the pointcut context of a CDS Hooks Prefetch Request
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsHookPrefetchPointcutContextJson implements IModelJson {

	public static final String TEMPLATE = "template";
	public static final String QUERY = "query";
	public static final String RESOLUTION_STRATEGY = "resolutionStrategy";
	public static final String USER_DATA = "userData";

	/**
	 * The prefetch template for the prefetch request
	 */
	@JsonProperty(value = TEMPLATE, required = true)
	String myTemplate;

	/**
	 * How the prefetch query will be executed (valid values include FHIR_CLIENT and DAO)
	 */
	@JsonProperty(value = RESOLUTION_STRATEGY, required = true)
	private CdsResolutionStrategyEnum myCdsResolutionStrategy;

	/**
	 * The actual prefetch query, generated based on the prefetch template using the prefetch context
	 */
	@JsonProperty(value = QUERY, required = true)
	String myQuery;

	/**
	 * Data to be stored between pointcut invocations of a prefetch request/response
	 */
	@JsonProperty(USER_DATA)
	private Map<String, Object> myUserData;

	public String getTemplate() {
		return myTemplate;
	}

	public void setTemplate(String theTemplate) {
		myTemplate = theTemplate;
	}

	public CdsResolutionStrategyEnum getCdsResolutionStrategy() {
		return myCdsResolutionStrategy;
	}

	public void setCdsResolutionStrategy(CdsResolutionStrategyEnum theCdsResolutionStrategy) {
		myCdsResolutionStrategy = theCdsResolutionStrategy;
	}

	public String getQuery() {
		return myQuery;
	}

	public void setQuery(String theQuery) {
		myQuery = theQuery;
	}

	public void addUserData(String theKey, Object theValue) {
		if (myUserData == null) {
			myUserData = new LinkedHashMap<>();
		}
		myUserData.put(theKey, theValue);
	}

	public Object getUserData(String theKey) {
		if (myUserData == null) {
			myUserData = new LinkedHashMap<>();
		}
		return myUserData.get(theKey);
	}
}
