package ca.uhn.fhir.rest.gclient;

/*
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

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IUntypedQuery<T> {

	IQuery<T> forAllResources();

	IQuery<T> forResource(String theResourceName);

	IQuery<T> forResource(Class<? extends IBaseResource> theClass);

	/**
	 * Perform a search directly by URL. It is usually better to construct the URL using the {@link #forAllResources()}, {@link #forResource(Class)} etc, but sometimes it is useful to simply search by
	 * entering a search URL directly.
	 * 
	 * @param theSearchUrl
	 *           The URL to search for. Note that this URL may be complete (e.g. "http://example.com/base/Patient?name=foo") in which case the client's base URL will be ignored. Or it can be relative
	 *           (e.g. "Patient?name=foo") in which case the client's base URL will be used.
	 */
	IQuery<T> byUrl(String theSearchUrl);

}
