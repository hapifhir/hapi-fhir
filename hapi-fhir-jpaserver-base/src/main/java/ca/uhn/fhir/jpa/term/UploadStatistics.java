package ca.uhn.fhir.jpa.term;

import org.hl7.fhir.instance.model.api.IIdType;

public class UploadStatistics {
	private final IIdType myTarget;
	private int myUpdatedConceptCount;

	public UploadStatistics(IIdType theTarget) {
		this(0, theTarget);
	}

	public UploadStatistics(int theUpdatedConceptCount, IIdType theTarget) {
		myUpdatedConceptCount = theUpdatedConceptCount;
		myTarget = theTarget;
	}

	public void incrementUpdatedConceptCount() {
		myUpdatedConceptCount++;
	}

	public int getUpdatedConceptCount() {
		return myUpdatedConceptCount;
	}

	public IIdType getTarget() {
		return myTarget;
	}

}
