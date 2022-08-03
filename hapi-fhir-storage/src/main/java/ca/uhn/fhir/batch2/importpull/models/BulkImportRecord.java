package ca.uhn.fhir.batch2.importpull.models;

import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportRecord implements IModelJson {
	/**
	 * Stringified version of the resource
	 */
	@JsonProperty("resource")
	private String myResourceString;

	/**
	 * Name of the tenant from the bulk import job file
	 */
	@JsonProperty("tenantName")
	private String myTenantName;

	/**
	 * The line index (starting at 1; for backwards compatibility)
	 * of the import file from which the resource was read.
	 */
	@JsonProperty("lineIndex")
	private int myLineIndex;

	/**
	 * The file index for the import job
	 */
	@JsonProperty("fileIndex")
	private int myFileIndex;

	/**
	 * Row processing mode
	 */
	@JsonProperty("rowProcessingMode")
	private JobFileRowProcessingModeEnum myProcessingMode;

	public String getResourceString() {
		return myResourceString;
	}

	public void setResourceString(String theResourceString) {
		myResourceString = theResourceString;
	}

	public String getTenantName() {
		return myTenantName;
	}

	public void setTenantName(String theTenantName) {
		myTenantName = theTenantName;
	}

	public int getLineIndex() {
		return myLineIndex;
	}

	public void setLineIndex(int theLineIndex) {
		myLineIndex = theLineIndex;
	}

	public int getFileIndex() {
		return myFileIndex;
	}

	public void setFileIndex(int theFileIndex) {
		myFileIndex = theFileIndex;
	}

	public JobFileRowProcessingModeEnum getProcessingMode() {
		return myProcessingMode;
	}

	public void setProcessingMode(JobFileRowProcessingModeEnum theProcessingMode) {
		myProcessingMode = theProcessingMode;
	}
}
