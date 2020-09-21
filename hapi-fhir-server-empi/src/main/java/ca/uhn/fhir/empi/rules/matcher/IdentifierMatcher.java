package ca.uhn.fhir.empi.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.util.IdentifierUtil;
import ca.uhn.fhir.model.primitive.IdentifierDt;
import org.hl7.fhir.instance.model.api.IBase;

public class IdentifierMatcher implements IEmpiFieldMatcher {
	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		IdentifierDt left = IdentifierUtil.identifierDtFromIdentifier(theLeftBase);
		IdentifierDt right = IdentifierUtil.identifierDtFromIdentifier(theRightBase);
		return left.equals(right);
	}
}
