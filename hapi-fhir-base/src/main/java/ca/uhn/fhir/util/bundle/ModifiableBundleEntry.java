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
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @deprecated Use {@link ca.uhn.fhir.util.BundleUtil#processAllEntries(FhirContext, IBaseBundle, Consumer)} instead
 */
@Deprecated(since = "8.6.0", forRemoval = true)
public class ModifiableBundleEntry {
	private final BundleEntryParts myBundleEntryParts;
	private final BundleEntryMutator myBundleEntryMutator;

	public ModifiableBundleEntry(BundleEntryParts theBundleEntryParts, BundleEntryMutator theBundleEntryMutator) {
		myBundleEntryParts = theBundleEntryParts;
		myBundleEntryMutator = theBundleEntryMutator;
	}

	BundleEntryParts getBundleEntryParts() {
		return myBundleEntryParts;
	}

	public void setRequestUrl(FhirContext theFhirContext, String theRequestUrl) {
		myBundleEntryMutator.setRequestUrl(theRequestUrl);
	}

	public String getFullUrl() {
		return myBundleEntryParts.getFullUrl();
	}

	public String getRequestUrl() {
		return myBundleEntryParts.getUrl();
	}

	public IBaseResource getResource() {
		return myBundleEntryParts.getResource();
	}

	public void setFullUrl(String theFullUrl) {
		myBundleEntryMutator.setFullUrl(theFullUrl);
	}

	public void setResource(IBaseResource theUpdatedResource) {
		myBundleEntryMutator.setResource(theUpdatedResource);
	}

	public RequestTypeEnum getRequestMethod() {
		return myBundleEntryParts.getRequestType();
	}

	public String getConditionalUrl() {
		return myBundleEntryParts.getConditionalUrl();
	}

	public void setRequestIfNoneMatch(FhirContext theFhirContext, String ifNoneMatch) {
		myBundleEntryMutator.setRequestIfNoneMatch(ifNoneMatch);
	}

	public void setRequestIfModifiedSince(FhirContext theFhirContext, Date theModifiedSince) {
		myBundleEntryMutator.setRequestIfModifiedSince(theModifiedSince);
	}

	public void setRequestIfMatch(FhirContext theFhirContext, String theIfMatch) {
		myBundleEntryMutator.setRequestIfMatch(theIfMatch);
	}

	public void setRequestIfNoneExist(FhirContext theFhirContext, String theIfNoneExist) {
		myBundleEntryMutator.setRequestIfNoneExist(theIfNoneExist);
	}
}
