package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseTerminologyImportParameters;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Properties;

public class ImportLoincJobParameters extends BaseTerminologyImportParameters {

	@JsonIgnore
	private Properties myJobProperties;

	public Properties getJobProperties() {
		return myJobProperties;
	}

	public void setJobProperties(Properties theJobProperties) {
		myJobProperties = theJobProperties;
	}
}
