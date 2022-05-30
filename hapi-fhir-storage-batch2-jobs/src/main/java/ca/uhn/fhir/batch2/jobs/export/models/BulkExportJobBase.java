package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkExportJobBase implements IModelJson {
	/**
	 * The id of the stored bulk export job
	 */
	@JsonProperty("bulkExportJobId")
	private String myJobId;

	// metadata job id
	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}
}
