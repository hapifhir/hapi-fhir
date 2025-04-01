package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportReportJson implements IModelJson {

	@JsonProperty("reportMsg")
	private String myReportMsg;

	public String getReportMsg() {
		return myReportMsg;
	}

	public void setReportMsg(String theReportMsg) {
		myReportMsg = theReportMsg;
	}
}
