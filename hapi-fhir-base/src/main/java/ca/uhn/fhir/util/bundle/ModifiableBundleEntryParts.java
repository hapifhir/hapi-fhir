/*-
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
package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Date;

/**
 * @since 8.6.0
 */
public class ModifiableBundleEntryParts extends BundleEntryParts {
	private final BundleEntryMutator myBundleEntryMutator;
	private final FhirContext myFhirContext;

	public ModifiableBundleEntryParts(
			FhirContext theFhirContext,
			BundleEntryParts theBundleEntryParts,
			BundleEntryMutator theBundleEntryMutator) {
		super(theBundleEntryParts);
		myFhirContext = theFhirContext;
		myBundleEntryMutator = theBundleEntryMutator;
	}

	/**
	 * Sets the conditional URL, following the same element selection logic as
	 * {@link #getConditionalUrl()}.
	 *
	 * @param theConditionalUrl The conditional URL. Must be in the format [resourceType]?[params]
	 */
	public void setConditionalUrl(@Nullable String theConditionalUrl) {
		Validate.isTrue(
				theConditionalUrl == null || theConditionalUrl.contains("?"),
				"theConditionalUrl must be null or be a valid conditional URL with format: [resourceType]?[params]");
		switch (getMethod()) {
			case POST -> setRequestIfNoneExist(theConditionalUrl);
			case PUT, PATCH, DELETE -> setRequestUrl(theConditionalUrl);
		}
	}

	public void setRequestUrl(String theRequestUrl) {
		myUrl = theRequestUrl;
		myBundleEntryMutator.setRequestUrl(theRequestUrl);
	}

	public void setFullUrl(String theFullUrl) {
		myFullUrl = theFullUrl;
		myBundleEntryMutator.setFullUrl(theFullUrl);
	}

	public void setResource(IBaseResource theUpdatedResource) {
		myBundleEntryMutator.setResource(theUpdatedResource);
	}

	public void setRequestIfNoneMatch(String ifNoneMatch) {
		myBundleEntryMutator.setRequestIfNoneMatch(ifNoneMatch);
	}

	public void setRequestIfModifiedSince(Date theModifiedSince) {
		myBundleEntryMutator.setRequestIfModifiedSince(theModifiedSince);
	}

	public void setRequestIfMatch(String theIfMatch) {
		myBundleEntryMutator.setRequestIfMatch(theIfMatch);
	}

	public void setRequestIfNoneExist(String theIfNoneExist) {
		myBundleEntryMutator.setRequestIfNoneExist(theIfNoneExist);
	}

	public void setMethod(RequestTypeEnum theMethod) {
		myMethod = theMethod;
		myBundleEntryMutator.setMethod(theMethod);
	}
}
