package ca.uhn.fhir.rest.api.server.matcher;

import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.IMdmFieldMatcher;
import ca.uhn.fhir.rest.api.server.matcher.models.MatchTypeEnum;

public interface IMatcherFactory {

	IMdmFieldMatcher getFieldMatcherForEnum(MatchTypeEnum theMdmMatcherEnum);
}
