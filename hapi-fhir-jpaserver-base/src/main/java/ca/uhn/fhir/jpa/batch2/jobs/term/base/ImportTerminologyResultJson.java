package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImportTerminologyResultJson implements IModelJson {

	@JsonProperty("report")
	private String myReport;

	public String getReport() {
		return myReport;
	}

	public void setReport(String theReport) {
		myReport = theReport;
	}
}
