package ca.uhn.fhir.batch2.jobs.export.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkExportExpandedResources extends BulkExportJobBase {

	/**
	 * List of stringified resources ready for writing
	 * to a file/binary.
	 */
	@JsonProperty("resources")
	private List<String> myStringifiedResources;

	public List<String> getStringifiedResources() {
		return myStringifiedResources;
	}

	public void setStringifiedResources(List<String> theStringifiedResources) {
		myStringifiedResources = theStringifiedResources;
	}
}
