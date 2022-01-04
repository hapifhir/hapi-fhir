package ca.uhn.fhir.rest.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Bundle provider that uses named pages instead of counts
 */
public class BundleProviderWithNamedPages extends SimpleBundleProvider {

	private String myNextPageId;
	private String myCurrentPageId;
	private String myPreviousPageId;

	/**
	 * Constructor
	 *
	 * @param theResultsInThisPage The complete list of results in the current page. Must not be null.
	 * @param theSearchId          The ID for the search. Note that you should also populate {@link #setNextPageId(String)} and {@link #setPreviousPageId(String)} if these are known. Must not be <code>null</code> or blank.
	 * @param thePageId            The ID for the current page. Note that you should also populate {@link #setNextPageId(String)} and {@link #setPreviousPageId(String)} if these are known. Must not be <code>null</code> or blank.
	 * @param theTotalResults      The total number of result (if this is known), or <code>null</code>
	 * @see #setNextPageId(String)
	 * @see #setPreviousPageId(String)
	 */
	public BundleProviderWithNamedPages(List<IBaseResource> theResultsInThisPage, String theSearchId, String thePageId, Integer theTotalResults) {
		super(theResultsInThisPage, theSearchId);

		Validate.notNull(theResultsInThisPage, "theResultsInThisPage must not be null");
		Validate.notBlank(thePageId, "thePageId must not be null or blank");

		setCurrentPageId(thePageId);
		setSize(theTotalResults);
	}

	@Override
	public String getCurrentPageId() {
		return myCurrentPageId;
	}

	public BundleProviderWithNamedPages setCurrentPageId(String theCurrentPageId) {
		myCurrentPageId = theCurrentPageId;
		return this;
	}

	@Override
	public String getNextPageId() {
		return myNextPageId;
	}

	public BundleProviderWithNamedPages setNextPageId(String theNextPageId) {
		myNextPageId = theNextPageId;
		return this;
	}

	@Override
	public String getPreviousPageId() {
		return myPreviousPageId;
	}

	public BundleProviderWithNamedPages setPreviousPageId(String thePreviousPageId) {
		myPreviousPageId = thePreviousPageId;
		return this;
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return (List<IBaseResource>) getList(); // indexes are ignored for this provider type
	}

	@Override
	public BundleProviderWithNamedPages setSize(Integer theSize) {
		super.setSize(theSize);
		return this;
	}

}
