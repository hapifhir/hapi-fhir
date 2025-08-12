package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkModifyResourcesResultsJson implements IModelJson {

	@JsonProperty("report")
	private String myReport;

	public String getReport() {
		return myReport;
	}

	public void setReport(String theReport) {
		myReport = theReport;
	}

}
