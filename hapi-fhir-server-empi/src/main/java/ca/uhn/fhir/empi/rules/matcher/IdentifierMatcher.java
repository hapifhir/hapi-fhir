package ca.uhn.fhir.empi.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.util.CanonicalIdentifier;
import ca.uhn.fhir.empi.util.IdentifierUtil;
import org.hl7.fhir.instance.model.api.IBase;

public class IdentifierMatcher implements IEmpiFieldMatcher {
	/**
	 *
	 * @return true if the two fhir identifiers are the same.  If @param theIdentifierSystem is not null, then the
	 * matcher only returns true if the identifier systems also match this system.
	 * @throws UnsupportedOperationException if either Base is not an Identifier instance
	 */
	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, String theIdentifierSystem) {
		CanonicalIdentifier left = IdentifierUtil.identifierDtFromIdentifier(theLeftBase);
		if (theIdentifierSystem != null) {
			if (!theIdentifierSystem.equals(left.getSystemElement().getValueAsString())) {
				return false;
			}
		}
		CanonicalIdentifier right = IdentifierUtil.identifierDtFromIdentifier(theRightBase);
		return left.equals(right);
	}
}
