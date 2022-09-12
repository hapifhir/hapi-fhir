package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.Validate;

/**
 * This interceptor for the HAPI FHIR JPA server forces all queries to
 * be performed as offset queries. This means that the query cache will
 * not be used and searches will never result in any writes to the
 * database.
 */
@Interceptor
public class ForceOffsetSearchModeInterceptor {

	/**
	 * Default value for {@link #setDefaultCount(Integer)}
	 */
	public static final int DEFAULT_DEFAULT_COUNT = 100;

	private Integer myDefaultCount = DEFAULT_DEFAULT_COUNT;

	public void setDefaultCount(Integer theDefaultCount) {
		Validate.notNull(theDefaultCount, "theDefaultCount must not be null");
		myDefaultCount = theDefaultCount;
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void storagePreSearchRegistered(SearchParameterMap theMap, RequestDetails theRequestDetails) {

		// If the params indicate a synchronous search, it doesn't make
		// sense to inject any offset processing since the search
		// will be handled synchronously anyhow
		if (theMap.isLoadSynchronous()) {
			return;
		}

		if (theMap.getOffset() == null) {
			theMap.setOffset(0);
		}
		if (theMap.getCount() == null) {
			theMap.setCount(myDefaultCount);
		}
	}


}
