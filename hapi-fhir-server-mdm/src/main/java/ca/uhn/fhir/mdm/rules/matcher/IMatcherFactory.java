package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;

public interface IMatcherFactory {

	/**
	 * Retrieves the field matcher for the given MatchTypeEnum
	 */
	IMdmFieldMatcher getFieldMatcherForMatchType(MatchTypeEnum theMdmMatcherEnum);
}
