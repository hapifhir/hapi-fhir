/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryTestCases {

	private final String myName;
	private final String myQuery;

	private QueryTestCases(String theName, String theQuery) {
		myName = theName;
		myQuery = theQuery;
	}

	public String getQuery() {
		return myQuery;
	}

	public String getName() {
		return myName;
	}

	@Override
	public String toString() {
		return myName;
	}

	public static List<QueryTestCases> get() {
		List<QueryTestCases> retVal = new ArrayList<>();

		retVal.add(create().withName("query string").withQuery("Patient?name=smith").build());
		retVal.add(create().withName("query date").withQuery("Observation?date=2021").build());
		retVal.add(create().withName("query token").withQuery("Patient?active=true").build());
		retVal.add(create().withName("query chained string").withQuery("Observation?subject.name=smith").build());
		retVal.add(create().withName("query chained token").withQuery("Observation?subject.identifier=http://foo|bar").build());
		retVal.add(create().withName("sort string").withQuery("Patient?_sort=name").build());
		retVal.add(create().withName("sort date").withQuery("Observation?_sort=date").build());
		retVal.add(create().withName("sort token").withQuery("Patient?_sort=active").build());
		retVal.add(create().withName("sort chained date").withQuery("Observation?_sort=patient.birthdate").build());
		retVal.add(create().withName("sort chained string").withQuery("Observation?_sort=patient.name").build());
		retVal.add(create().withName("sort chained qualified").withQuery("Patient?_sort=Practitioner:general-practitioner.family").build());
		retVal.add(create().withName("sort chained token").withQuery("Observation?_sort=patient.active").build());
		retVal.add(create().withName("has reference").withQuery("Patient?_has:Observation:subject:device=Device/123").build());
		retVal.add(create().withName("has chained token").withQuery("Patient?_has:Observation:subject:device.identifier=1234-5").build());
		return retVal;
	}


	public static QueryTestCasesBuilder create() {
		return new QueryTestCasesBuilder();
	}


	public static final class QueryTestCasesBuilder {
		private String myName;
		private String myQuery;

		private QueryTestCasesBuilder() {
		}

		public QueryTestCasesBuilder withName(String myName) {
			this.myName = myName;
			return this;
		}

		public QueryTestCasesBuilder withQuery(String myQuery) {
			this.myQuery = myQuery;
			return this;
		}

		public QueryTestCases build() {
			return new QueryTestCases(myName, myQuery);
		}

	}
}
