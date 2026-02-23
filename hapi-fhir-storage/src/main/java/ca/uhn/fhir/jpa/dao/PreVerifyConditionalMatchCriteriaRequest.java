/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This class is a request object for the {@link Pointcut#STORAGE_PREVERIFY_CONDITIONAL_MATCH_CRITERIA}
 * pointcut.
 *
 * @since 8.8.0
 */
public class PreVerifyConditionalMatchCriteriaRequest {

	private final IBaseResource myResource;

	private String myConditionalUrl;

	public PreVerifyConditionalMatchCriteriaRequest(IBaseResource theResource) {
		myResource = theResource;
	}

	/**
	 * Provides the conditional URL that will be used for verification. This can be modified by calling
	 * {@link #setConditionalUrl(String)}
	 */
	public String getConditionalUrl() {
		return myConditionalUrl;
	}

	/**
	 * Provides a new conditional URL that will be used for verification.
	 */
	public void setConditionalUrl(String theConditionalUrl) {
		myConditionalUrl = theConditionalUrl;
	}

	/**
	 * Provides the resource that will be verified. Hooks should not modify this object or
	 * unspecified behaviour may result.
	 */
	public IBaseResource getResource() {
		return myResource;
	}
}
