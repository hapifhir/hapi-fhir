package ca.uhn.fhir.empi.rules.json;

import org.hl7.fhir.instance.model.api.IBase;

public class NameAnyOrderSimilarity implements EmpiFieldSimilarity {
	@Override
	public double similarity(IBase theLeftBase, IBase theRightBase) {

		return 0;
	}
}
