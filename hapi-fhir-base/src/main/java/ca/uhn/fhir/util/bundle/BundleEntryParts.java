package ca.uhn.fhir.util.bundle;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class BundleEntryParts {
	private final RequestTypeEnum myRequestType;
	private final IBaseResource myResource;
	private final String myUrl;
	private final String myConditionalUrl;
	private final String myFullUrl;

	public BundleEntryParts(String theFullUrl, RequestTypeEnum theRequestType, String theUrl, IBaseResource theResource, String theConditionalUrl) {
		super();
		myFullUrl = theFullUrl;
		myRequestType = theRequestType;
		myUrl = theUrl;
		myResource = theResource;
		myConditionalUrl = theConditionalUrl;
	}

	public String getFullUrl() {
		return myFullUrl;
	}

	public RequestTypeEnum getRequestType() {
		return myRequestType;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public String getConditionalUrl() {
		return myConditionalUrl;
	}

	public String getUrl() {
		return myUrl;
	}
}
