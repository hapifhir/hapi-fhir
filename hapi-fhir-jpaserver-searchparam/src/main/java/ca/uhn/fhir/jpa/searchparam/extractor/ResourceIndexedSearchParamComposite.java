/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Intermediate holder for indexing composite search parameters.
 * There will be one instance for each element extracted by the parent composite path expression.
 */
public class ResourceIndexedSearchParamComposite {

	private final String mySearchParamName;
	private final String myPath;
	private final List<Component> myComponents = new ArrayList<>();

	public ResourceIndexedSearchParamComposite(String theSearchParamName, String thePath) {
		mySearchParamName = theSearchParamName;
		myPath = thePath;
	}

	/**
	 * the SP name for this composite SP
	 */
	public String getSearchParamName() {
		return mySearchParamName;
	}

	/**
	 * The path expression of the composite SP
	 */
	public String getPath() {
		return myPath;
	}

	/**
	 * Subcomponent index data for this composite
	 */
	public List<Component> getComponents() {
		return myComponents;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Add subcomponent index data.
	 * @param theComponentSearchParam the component SP we are extracting
	 * @param theExtractedParams index data extracted by the sub-extractor
	 */
	public void addComponentIndexedSearchParams(
			RuntimeSearchParam theComponentSearchParam,
			ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theExtractedParams) {
		addComponentIndexedSearchParams(
				theComponentSearchParam.getName(), theComponentSearchParam.getParamType(), theExtractedParams);
	}

	public void addComponentIndexedSearchParams(
			String theComponentSearchParamName,
			RestSearchParameterTypeEnum theComponentSearchParamType,
			ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theExtractedParams) {
		myComponents.add(new Component(theComponentSearchParamName, theComponentSearchParamType, theExtractedParams));
	}

	/**
	 * Nested holder of index data for a single component of a composite SP.
	 * E.g. hold token info for component-code under parent of component-code-value-quantity.
	 */
	public static class Component {
		/**
		 * The SP name of this subcomponent.
		 * E.g. "component-code" when the parent composite SP is component-code-value-quantity.
		 */
		private final String mySearchParamName;
		/**
		 * The SP type of this subcomponent.
		 * E.g. TOKEN when indexing "component-code" of parent composite SP is component-code-value-quantity.
		 */
		private final RestSearchParameterTypeEnum mySearchParameterType;
		/**
		 * Any of the extracted data of any type for this subcomponent.
		 */
		private final ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> myParamIndexValues;

		private Component(
				String theComponentSearchParamName,
				RestSearchParameterTypeEnum theComponentSearchParamType,
				ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theParamIndexValues) {
			mySearchParamName = theComponentSearchParamName;
			mySearchParameterType = theComponentSearchParamType;
			myParamIndexValues = theParamIndexValues;
		}

		public String getSearchParamName() {
			return mySearchParamName;
		}

		public RestSearchParameterTypeEnum getSearchParameterType() {
			return mySearchParameterType;
		}

		public ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> getParamIndexValues() {
			return myParamIndexValues;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
}
