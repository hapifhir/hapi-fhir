package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.searchparam.matcher.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;

public interface IMatcherFactory {

	/**
	 * Retrieves the field matcher for the given MatchTypeEnum
	 */
	IMdmFieldMatcher getFieldMatcherForEnum(MatchTypeEnum theMdmMatcherEnum);
}
