package ca.uhn.fhir.narrative;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.parser.DataFormatException;

public interface INarrativeGenerator {

	public NarrativeDt generateNarrative(String theProfile, IResource theResource) throws DataFormatException;
	
}
