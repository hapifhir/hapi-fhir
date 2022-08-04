package ca.uhn.fhir.batch2.importpull.models;

import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportFilePartitionResult implements IModelJson {

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

	/**
	 * The job description
	 */
	@JsonProperty("jobDescription")
	private String myJobDescription;

	/**
	 * The file description
	 */
	@JsonProperty("fileDescription")
	private String myFileDescription;

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

	public String getJobDescription() {
		return myJobDescription;
	}

	public void setJobDescription(String theJobDescription) {
		myJobDescription = theJobDescription;
	}

	public String getFileDescription() {
		return myFileDescription;
	}

	public void setFileDescription(String theFileDescription) {
		myFileDescription = theFileDescription;
	}
}
