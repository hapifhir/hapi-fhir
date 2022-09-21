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

	/**
	 * the SP name for this composite SP
	 */
	private final String mySearchParamName;

	/**
	 * The path expression of the composite SP
	 */
	private final String myPath;

	/**
	 * Subcomponent index data for this composite
	 */
	private final List<Component> myComponents = new ArrayList<>();

	public ResourceIndexedSearchParamComposite(String theSearchParamName, String thePath) {
		mySearchParamName = theSearchParamName;
		myPath = thePath;
	}

	public String getSearchParamName() {
		return mySearchParamName;
	}

	public String getPath() {
		return myPath;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public List<Component> getComponents() {
		return myComponents;
	}

	/**
	 * Add subcomponent index data.
	 * @param theComponentSearchParam the component SP we are extracting
	 * @param theExtractedParams index data extracted by the sub-extractor
	 */
	public void addComponent(RuntimeSearchParam theComponentSearchParam, ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theExtractedParams) {
		addComponent(theComponentSearchParam.getName(), theComponentSearchParam.getParamType(), theExtractedParams);
	}

	public void addComponent(String theComponentSearchParamName, RestSearchParameterTypeEnum theComponentSearchParamType, ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theExtractedParams) {
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
		final String mySearchParamName;
		/**
		 * The SP type of this subcomponent.
		 * E.g. TOKEN when indexing "component-code" of parent composite SP is component-code-value-quantity.
		 */
		final RestSearchParameterTypeEnum mySearchParameterType;
		/**
		 * Any of the extracted data of any type for this subcomponent.
		 */
		final ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> myParamIndexValues;

		public Component(String theComponentSearchParamName, RestSearchParameterTypeEnum theComponentSearchParamType, ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theParamIndexValues) {
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
