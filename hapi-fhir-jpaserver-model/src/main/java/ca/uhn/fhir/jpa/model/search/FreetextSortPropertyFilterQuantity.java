package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;

/**
 * Selects which set of properties to return based on the registered properties
 * for QUANTITY type parameter
 */
public class FreetextSortPropertyFilterQuantity implements IFreetextSortPropertyFilter {

	@Override
	public boolean accepts(RestSearchParameterTypeEnum theParamPropType) {
		return theParamPropType == RestSearchParameterTypeEnum.QUANTITY;
	}


	/**
	 * If there is a normalized quantity (any entry ends with QTY_VALUE_NORM), return it. Otherwise,
	 * return the not normalized (QTY_VALUE) entry. Note that sorting by not-normalized value is meaningless
	 * in case units are not all the same.
	 */
	@Override
	public List<String> filter(List<String> theFieldPathList) {
		String theNotNormalizedPath = null;

		for (String fieldPath : theFieldPathList) {
			if (fieldPath.endsWith(QTY_VALUE_NORM)) {
				return Collections.singletonList(fieldPath);
			}

			if (fieldPath.endsWith(QTY_VALUE)) {
				theNotNormalizedPath = fieldPath;
			}
		}

		return theNotNormalizedPath == null ? Collections.emptyList() : Collections.singletonList(theNotNormalizedPath);
	}
}
