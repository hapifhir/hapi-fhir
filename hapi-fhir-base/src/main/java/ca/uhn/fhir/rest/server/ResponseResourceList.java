package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Used by {@link IBundleProvider} to provide a single page worth of results.
 * 
 * If the server chooses to, it may return a different number of matching results to the number that the user requested.
 * For example, if the client requested 100 results but the server decided to return only 10 (perhaps because they were
 * very large), this value should be set to 10. Note that this count refers only to resources which are included in the
 * indexes provided to {@link IBundleProvider#getResources(int, int)}, so it should not reflect any additional results
 * added to the response as a result of _include parameters, OperationOutcome's etc.
 */
public class ResponseResourceList {
	/**
	 * Singleton unmodifiable empty list
	 */
	public static final ResponseResourceList EMPTY = new EmptyResponseResourceList();

	private List<IBaseResource> myIncludeResults;	
	private List<IBaseResource> myMatchResults;
	
	/**
	 * Adds an "include" results. Include results are results which are added as a result of <code>_include</code>
	 * directives in search requests.
	 */
	public void addIncludeResults(IBaseResource theIncludeResult) {
		if (myIncludeResults == null) {
			myIncludeResults = new ArrayList<IBaseResource>();
		}
		myIncludeResults.add(theIncludeResult);
	}

	/**
	 * Adds a "match" result. A match result is a result added as a direct result of the operation in question. E.g. for
	 * a search invocation a match result would be a result which directly matched the search criteria. For a history
	 * invocation it would be a historical version of a resource or the current version.
	 */
	public void addMatchResult(IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		if (myMatchResults == null) {
			myMatchResults = new ArrayList<IBaseResource>();
		}
		myMatchResults.add(theResource);
	}

	public List<IBaseResource> getIncludeResults() {
		return myIncludeResults;
	}

	public List<IBaseResource> getMatchResults() {
		return myMatchResults;
	}

	/**
	 * Sets the "include" results. Include results are results which are added as a result of <code>_include</code>
	 * directives in search requests.
	 */
	public void setIncludeResults(List<IBaseResource> theIncludeResults) {
		myIncludeResults = theIncludeResults;
	}

	/**
	 * Sets the "match" results. A match result is a result added as a direct result of the operation in question. E.g.
	 * for a search invocation a match result would be a result which directly matched the search criteria. For a
	 * history invocation it would be a historical version of a resource or the current version.
	 */
	public void setMatchResults(List<IBaseResource> theMatchResults) {
		myMatchResults = theMatchResults;
	}

	private static final class EmptyResponseResourceList extends ResponseResourceList {
		@Override
		public void addIncludeResults(IBaseResource theIncludeResult) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addMatchResult(IBaseResource theResource) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIncludeResults(List<IBaseResource> theIncludeResults) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMatchResults(List<IBaseResource> theMatchResults) {
			throw new UnsupportedOperationException();
		}
	}

}
