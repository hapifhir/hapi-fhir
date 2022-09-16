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
 */
public class ResourceIndexedSearchParamComposite {
	/**
	 * the SP name
	 */
	private final String mySearchParamName;

	private final String myResourceType;
	/**
	 * The path of the parent element
	 */
	private final String myPath;
	private List<Component> myComponents = new ArrayList<>();

	public static class Component {
		final String mySearchParamName;
		final RestSearchParameterTypeEnum mySearchParameterType;
		final ISearchParamExtractor.SearchParamSet<? extends BaseResourceIndexedSearchParam> myParamIndexValues;

		public Component(String theSearchParamName, RestSearchParameterTypeEnum theSearchParameterType, ISearchParamExtractor.SearchParamSet<? extends BaseResourceIndexedSearchParam> theParamIndexValues) {
			mySearchParamName = theSearchParamName;
			mySearchParameterType = theSearchParameterType;
			myParamIndexValues = theParamIndexValues;
		}

		public String getSearchParamName() {
			return mySearchParamName;
		}

		public RestSearchParameterTypeEnum getSearchParameterType() {
			return mySearchParameterType;
		}

		public ISearchParamExtractor.SearchParamSet<? extends BaseResourceIndexedSearchParam> getParamIndexValues() {
			return myParamIndexValues;
		}
	}

	public ResourceIndexedSearchParamComposite(String theSearchParamName, String theResourceType, String thePath) {
		mySearchParamName = theSearchParamName;
		myResourceType = theResourceType;
		myPath = thePath;
	}


	public String getSearchParamName() {
		return mySearchParamName;
	}

	public String getResourceType() {
		return myResourceType;
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


	public void addComponent(RuntimeSearchParam theRuntimeSearchParam, ISearchParamExtractor.SearchParamSet<? extends BaseResourceIndexedSearchParam> theExtractedParams) {
		addComponent(theRuntimeSearchParam.getName(), theRuntimeSearchParam.getParamType(), theExtractedParams);
	}

	public void addComponent(String theSearchParameterName, RestSearchParameterTypeEnum theSearchParameterType, ISearchParamExtractor.SearchParamSet<? extends BaseResourceIndexedSearchParam> theExtractedParams) {
		// wipmb theSearchParameterName is likely redundant since it will be in the params
		myComponents.add(new Component(theSearchParameterName, theSearchParameterType, theExtractedParams));
	}


}
