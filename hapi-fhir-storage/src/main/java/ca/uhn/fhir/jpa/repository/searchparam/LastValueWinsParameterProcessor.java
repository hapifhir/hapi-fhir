/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.repository.searchparam.ISpecialParameterProcessor.paramAsQueryString;

/**
 * A processor that takes the last value of a parameter and converts it to a type T while treating null or blank as null.
 * @param <T> the type used in the SearchParameterMap setter.
 */
class LastValueWinsParameterProcessor<T> implements ISpecialParameterProcessor {
	private final Function<String, T> myConverter;
	private final BiConsumer<SearchParameterMap, T> mySearchParameterMapSetter;

	LastValueWinsParameterProcessor(
			Function<String, T> theConverter, BiConsumer<SearchParameterMap, T> theSearchParameterMapSetter) {
		myConverter = theConverter;
		mySearchParameterMapSetter = theSearchParameterMapSetter;
	}

	@Override
	public void process(
			String theParamName, List<IQueryParameterType> theValues, SearchParameterMap theSearchParameterMap) {

		T converted = null;
		if (CollectionUtils.isNotEmpty(theValues)) {
			IQueryParameterType lastParameter = theValues.get(theValues.size() - 1);
			String lastValue = paramAsQueryString(lastParameter);
			converted = myConverter.apply(lastValue);
		}

		mySearchParameterMapSetter.accept(theSearchParameterMap, converted);
	}

	/**
	 * Build a processor that takes the last value of a parameter, converts it to a type,
	 * and sets a single value on the SearchParameterMap.
	 * Treats null or blank values as null.
	 * @param <T> the type used in the SearchParameterMap setter.
	 */
	public static <T> ISpecialParameterProcessor lastValueWins(
			Function<String, T> theConverter, BiConsumer<SearchParameterMap, T> theSearchParameterMapSetter) {
		return new LastValueWinsParameterProcessor<>(theConverter, theSearchParameterMapSetter);
	}
}
