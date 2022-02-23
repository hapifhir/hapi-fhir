package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.List;
import java.util.Map;

/*
 * #%L
 * HAPI FHIR - Core Library
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

public interface IBaseQuery<T extends IBaseQuery<?>> {

	/**
	 * Add a search parameter to the query.
	 * <p>
	 * Note that this method is a synonym for {@link #where(ICriterion)}, and is only
	 * here to make fluent queries read more naturally.
	 * </p>
	 */
	T and(ICriterion<?> theCriterion);

	/**
	 * Add a set of search parameters to the query.
	 *
	 * Note that the entries of the map are extracted immediately upon invoking this method. Changes made to the
	 * map afterward will not be reflected in the actual search.
	 */
	T where(Map<String, List<IQueryParameterType>> theCriterion);

	/**
	 * Add a search parameter to the query.
	 */
	T where(ICriterion<?> theCriterion);

	/**
	 * Add a set of search parameters to the query.
	 * <p>
	 * Values will be treated semi-literally. No FHIR escaping will be performed
	 * on the values, but regular URL escaping will be.
	 * </p>
	 */
	T whereMap(Map<String, List<String>> theRawMap);

}
