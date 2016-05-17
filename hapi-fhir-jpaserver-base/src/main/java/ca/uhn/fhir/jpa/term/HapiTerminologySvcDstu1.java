package ca.uhn.fhir.jpa.term;

import java.util.List;

public class HapiTerminologySvcDstu1 extends BaseHapiTerminologySvc {

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		throw new UnsupportedOperationException();
	}

}
