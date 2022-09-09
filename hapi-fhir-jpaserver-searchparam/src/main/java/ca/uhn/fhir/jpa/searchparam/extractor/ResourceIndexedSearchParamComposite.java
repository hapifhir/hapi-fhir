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
	private final String myParamName;

	private final String myResourceType;
	/**
	 * The path of the parent element
	 */
	private final String myPath;
	private List<Component> myComponents = new ArrayList<>();

	public static class Component {
		final String mySearchParamName;
		final RestSearchParameterTypeEnum mySearchParameterType;
		final ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> myParamIndexValues;

		public Component(String theSearchParamName, RestSearchParameterTypeEnum theSearchParameterType, ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theParamIndexValues) {
			mySearchParamName = theSearchParamName;
			mySearchParameterType = theSearchParameterType;
			myParamIndexValues = theParamIndexValues;
		}
	}

	public ResourceIndexedSearchParamComposite(String theParamName, String theResourceType, String thePath) {
		myParamName = theParamName;
		myResourceType = theResourceType;
		myPath = thePath;
	}


	public String getParamName() {
		return myParamName;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public String getPath() {
		return myPath;
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myParamName", myParamName)
			.append("myResourceType", myResourceType)
			.append("myPath", myPath)
			.toString();
	}

	public List<Component> getComponents() {
		return myComponents;
	}


	public void addComponent(RuntimeSearchParam theRuntimeSearchParam, ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> theExtractedParams) {
		myComponents.add(new Component(theRuntimeSearchParam.getName(), theRuntimeSearchParam.getParamType(), theExtractedParams));
	}


}
