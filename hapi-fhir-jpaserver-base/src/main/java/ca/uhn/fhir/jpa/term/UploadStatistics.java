package ca.uhn.fhir.jpa.term;

import org.hl7.fhir.instance.model.api.IIdType;

public class UploadStatistics {
	private final int myConceptCount;
	private final IIdType myTarget;

	public UploadStatistics(int theConceptCount, IIdType theTarget) {
		myConceptCount = theConceptCount;
		myTarget = theTarget;
	}

	public int getConceptCount() {
		return myConceptCount;
	}

	public IIdType getTarget() {
		return myTarget;
	}

}
