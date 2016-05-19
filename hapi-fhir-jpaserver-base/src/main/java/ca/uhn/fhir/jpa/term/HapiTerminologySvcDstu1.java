package ca.uhn.fhir.jpa.term;

import java.util.List;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;

public class HapiTerminologySvcDstu1 extends BaseHapiTerminologySvc {

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void storeNewCodeSystemVersion(String theSystem, TermCodeSystemVersion theCodeSystemVersion) {
		throw new UnsupportedOperationException();
	}

}
