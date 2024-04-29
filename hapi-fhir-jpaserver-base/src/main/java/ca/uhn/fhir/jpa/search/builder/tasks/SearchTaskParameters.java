/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.tasks;

import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.function.Consumer;

public class SearchTaskParameters {
	// parameters
	public ca.uhn.fhir.jpa.entity.Search Search;
	public IDao CallingDao;
	public SearchParameterMap Params;
	public String ResourceType;
	public RequestDetails Request;
	public ca.uhn.fhir.interceptor.model.RequestPartitionId RequestPartitionId;
	public Consumer<String> OnRemove;
	public int SyncSize;

	private Integer myLoadingThrottleForUnitTests;

	public SearchTaskParameters(
			ca.uhn.fhir.jpa.entity.Search theSearch,
			IDao theCallingDao,
			SearchParameterMap theParams,
			String theResourceType,
			RequestDetails theRequest,
			ca.uhn.fhir.interceptor.model.RequestPartitionId theRequestPartitionId,
			Consumer<String> theOnRemove,
			int theSyncSize) {
		Search = theSearch;
		CallingDao = theCallingDao;
		Params = theParams;
		ResourceType = theResourceType;
		Request = theRequest;
		RequestPartitionId = theRequestPartitionId;
		OnRemove = theOnRemove;
		SyncSize = theSyncSize;
	}

	public Integer getLoadingThrottleForUnitTests() {
		return myLoadingThrottleForUnitTests;
	}

	public void setLoadingThrottleForUnitTests(Integer theLoadingThrottleForUnitTests) {
		myLoadingThrottleForUnitTests = theLoadingThrottleForUnitTests;
	}
}
