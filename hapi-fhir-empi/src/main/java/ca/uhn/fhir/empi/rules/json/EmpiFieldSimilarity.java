package ca.uhn.fhir.empi.rules.json;

import org.hl7.fhir.instance.model.api.IBase;

public interface EmpiFieldSimilarity {
	double similarity(IBase theLeftBase, IBase theRightBase);
}
