package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.StopWatch;

import javax.annotation.Nullable;

/**
 * This class contains a runtime in-memory description of a search operation,
 * including details on processing time and other things
 */
public class SearchRuntimeDetails {
	private final String mySearchUuid;
	private final RequestDetails myRequestDetails;
	private StopWatch myQueryStopwatch;
	private int myFoundMatchesCount;
	private boolean myLoadSynchronous;
	private String myQueryString;
	private SearchStatusEnum mySearchStatus;
	private int myFoundIndexMatchesCount;

	public SearchRuntimeDetails(RequestDetails theRequestDetails, String theSearchUuid) {
		myRequestDetails = theRequestDetails;
		mySearchUuid = theSearchUuid;
	}

	@Nullable
	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public String getSearchUuid() {
		return mySearchUuid;
	}

	public StopWatch getQueryStopwatch() {
		return myQueryStopwatch;
	}

	public void setQueryStopwatch(StopWatch theQueryStopwatch) {
		myQueryStopwatch = theQueryStopwatch;
	}

	public int getFoundMatchesCount() {
		return myFoundMatchesCount;
	}

	public void setFoundMatchesCount(int theFoundMatchesCount) {
		myFoundMatchesCount = theFoundMatchesCount;
	}

	public int getFoundIndexMatchesCount() {
		return myFoundIndexMatchesCount;
	}

	public void setFoundIndexMatchesCount(int theFoundIndexMatchesCount) {
		myFoundIndexMatchesCount = theFoundIndexMatchesCount;
	}

	public boolean getLoadSynchronous() {
		return myLoadSynchronous;
	}

	public void setLoadSynchronous(boolean theLoadSynchronous) {
		myLoadSynchronous = theLoadSynchronous;
	}

	public String getQueryString() {
		return myQueryString;
	}

	public void setQueryString(String theQueryString) {
		myQueryString = theQueryString;
	}

	public SearchStatusEnum getSearchStatus() {
		return mySearchStatus;
	}

	public void setSearchStatus(SearchStatusEnum theSearchStatus) {
		mySearchStatus = theSearchStatus;
	}
}
