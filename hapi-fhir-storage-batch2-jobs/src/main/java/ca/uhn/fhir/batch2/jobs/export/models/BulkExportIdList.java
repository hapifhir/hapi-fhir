package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.batch2.jobs.models.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkExportIdList extends BulkExportJobBase {

	/**
	 * List of Id objects for serialization
	 */
	@JsonProperty("ids")
	private List<Id> myIds;

	public List<Id> getIds() {
		return myIds;
	}

	public void setIds(List<Id> theIds) {
		myIds = theIds;
	}
}
