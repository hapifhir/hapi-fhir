package ca.uhn.fhir.narrative;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;

public interface INarrativeGenerator {

	public NarrativeDt generateNarrative(String theProfile, IResource theResource);
	
}
