package ca.uhn.fhir.empi.rules.similarity;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;

public class ReferenceMatchSimilarity implements IEmpiFieldSimilarity {
	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase) {
		System.out.println("wip!");
		return 1;
	}
}
