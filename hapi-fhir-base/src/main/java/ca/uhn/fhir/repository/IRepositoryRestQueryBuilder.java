/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.repository;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

/**
 * Abstract interface for building a repository rest query.
 */
public interface IRepositoryRestQueryBuilder {

	/**
	 * The main method for implementations to add a parameter to the query.
	 * @param theParamName the search parameter name, without modifiers.  E.g. "name", or "_sort"
	 * @param theParameters a list of parameters - this is the comma-separated list after the "=" in a rest query.
	 * @return this for chaining
	 */
	IRepositoryRestQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theParameters);

	default IRepositoryRestQueryBuilder addOrList(String theParamName, IQueryParameterType... theParameterValues) {
		return addOrList(theParamName, List.of(theParameterValues));
	}

	default IRepositoryRestQueryBuilder addNumericParameter(String theParamName, int theValue) {
		return addOrList(theParamName, new NumberParam(theValue));
	}

	default IRepositoryRestQueryBuilder addAll(Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		theSearchParameters.entries().forEach(e -> this.addOrList(e.getKey(), e.getValue()));
		return this;
	}

	default IRepositoryRestQueryBuilder addAll(Map<String, List<IQueryParameterType>> theSearchParameters) {
		theSearchParameters.forEach(this::addOrList);
		return this;
	}
}
