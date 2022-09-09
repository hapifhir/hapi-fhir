package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import javax.persistence.Column;

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
}
