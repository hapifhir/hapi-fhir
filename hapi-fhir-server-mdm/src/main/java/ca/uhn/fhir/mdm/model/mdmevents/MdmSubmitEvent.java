package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmSubmitEvent implements IModelJson {

	/**
	 * Batch size; only applicable if this is a batch job
	 */
	@JsonProperty("batchSize")
	private Long myBatchSize;

	/**
	 * The search/resource urls used in this job
	 */
	@JsonProperty("urls")
	private List<String> myUrls;

	/**
	 * True if this submit was done asynchronously
	 * (ie, was submitted as a batch job).
	 * False if submitted directly to mdm.
	 */
	@JsonProperty("batch_job")
	private boolean myIsBatchJob;

	public Long getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(Long theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public List<String> getUrls() {
		if (myUrls == null) {
			myUrls = new ArrayList<>();
		}
		return myUrls;
	}

	public void setUrls(List<String> theUrls) {
		myUrls = theUrls;
	}

	public MdmSubmitEvent addUrl(String theUrl) {
		getUrls().add(theUrl);
		return this;
	}

	public boolean isBatchJob() {
		return myIsBatchJob;
	}

	public void setBatchJob(boolean theBatchJob) {
		myIsBatchJob = theBatchJob;
	}
}
