package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.searchparam.matcher.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;

public interface IMatcherFactory {

	IMdmFieldMatcher getFieldMatcherForEnum(MatchTypeEnum theMdmMatcherEnum);
}
