package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;

public class EmptyFieldMatcher implements IMdmFieldMatcher {

	public EmptyFieldMatcher() {
	}

	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, String theIdentifierSystem) {
		for (IBase b : new IBase[] {theLeftBase, theRightBase}) {
			if (b != null && !b.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
