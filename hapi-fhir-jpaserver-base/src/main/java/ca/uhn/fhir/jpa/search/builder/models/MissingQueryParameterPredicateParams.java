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
package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;

public class MissingQueryParameterPredicateParams {
	/**
	 * Base ResourceTable predicate builder
	 */
	private ResourceTablePredicateBuilder myResourceTablePredicateBuilder;
	/**
	 * The missing boolean.
	 * True if looking for missing fields.
	 * False if looking for non-missing fields.
	 */
	private boolean myIsMissing;
	/**
	 * The Search Parameter Name
	 */
	private String myParamName;
	/**
	 * The partition id
	 */
	private RequestPartitionId myRequestPartitionId;

	public MissingQueryParameterPredicateParams(
			ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder theResourceTablePredicateBuilder,
			boolean theTheMissing,
			String theParamName,
			ca.uhn.fhir.interceptor.model.RequestPartitionId theRequestPartitionId) {
		myResourceTablePredicateBuilder = theResourceTablePredicateBuilder;
		myIsMissing = theTheMissing;
		myParamName = theParamName;
		myRequestPartitionId = theRequestPartitionId;
	}

	public ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder getResourceTablePredicateBuilder() {
		return myResourceTablePredicateBuilder;
	}

	public boolean isMissing() {
		return myIsMissing;
	}

	public String getParamName() {
		return myParamName;
	}

	public ca.uhn.fhir.interceptor.model.RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}
}
