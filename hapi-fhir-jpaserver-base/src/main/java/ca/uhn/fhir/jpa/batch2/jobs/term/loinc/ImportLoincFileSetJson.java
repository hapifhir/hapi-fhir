package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;

public class ImportLoincFileSetJson extends TerminologyFileSetJson {

	@Override
	public TerminologyFileSetJson cloneWithOnlyCopyForwardData() {
		return new TerminologyFileSetJson(super.cloneWithOnlyCopyForwardData());
	}

}
