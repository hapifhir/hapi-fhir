/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO JDJD 1028 likely remove this class
 * Tester that a resource matches any query filter in a list.
 */
public class FhirQueriesRuleTester implements IAuthRuleTester {
	private final List<FhirQueryRuleTester> myFilters;
	private final String myResourceType;

	public FhirQueriesRuleTester(List<String> theQueries, String theResourceType) {
		myFilters = theQueries.stream()
				.map(query -> query.contains("?") ? query.substring(query.indexOf("?") + 1) : query)
				.map(FhirQueryRuleTester::new)
				.collect(Collectors.toList());
		myResourceType = theResourceType;
	}

	public void addFilter(FhirQueriesRuleTester theOtherRuleTester) {
		// todo jdjd can i add to list?
		if (theOtherRuleTester.getResourceType().equals(myResourceType)) {
			myFilters.addAll(theOtherRuleTester.getFilters());
		}
	}

	public List<FhirQueryRuleTester> getFilters() {
		return List.copyOf(myFilters);
	}

	public List<String> getFiltersAsString() {
		return myFilters.stream()
				.map(param -> myResourceType + '?' + param.getQueryParameters())
				.toList();
	}

	public String getResourceType() {
		return myResourceType;
	}

	@Override
	public boolean matches(RuleTestRequest theRuleTestRequest) {
		return myFilters.stream().anyMatch(t -> t.matches(theRuleTestRequest));
	}

	@Override
	public boolean matchesOutput(RuleTestRequest theRuleTestRequest) {
		return myFilters.stream().anyMatch(t -> t.matches(theRuleTestRequest));
	}

	// todo jdjds what is to string used for and what should the implementation look like for a list of queries
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("filter", myFilters)
				.toString();
	}
}
