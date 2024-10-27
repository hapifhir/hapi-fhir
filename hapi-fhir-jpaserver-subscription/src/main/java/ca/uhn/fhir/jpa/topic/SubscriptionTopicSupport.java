/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;

public class SubscriptionTopicSupport {
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final SearchParamMatcher mySearchParamMatcher;

	public SubscriptionTopicSupport(
			FhirContext theFhirContext, DaoRegistry theDaoRegistry, SearchParamMatcher theSearchParamMatcher) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		mySearchParamMatcher = theSearchParamMatcher;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	public SearchParamMatcher getSearchParamMatcher() {
		return mySearchParamMatcher;
	}
}
