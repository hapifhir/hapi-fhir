package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;

import java.util.Date;

public class Batch2JobInfo {

	// job instance id
	private String myJobId;

	// job status - see StatusEnum
	private BulkExportJobStatusEnum myStatus;

	// cancelled boolean
	private boolean myCancelled;

	// start time
	private Date myStartTime;

	// end time
	private Date myEndTime;

	// error message
	private String myErrorMsg;

	// the output report (stringified json of whatever the reduction step outputs)
	private String myReport;

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public BulkExportJobStatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(BulkExportJobStatusEnum theStatus) {
		myStatus = theStatus;
	}

	public boolean isCancelled() {
		return myCancelled;
	}

	public void setCancelled(boolean theCancelled) {
		myCancelled = theCancelled;
	}

	public Date getStartTime() {
		return myStartTime;
	}

	public void setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
	}

	public Date getEndTime() {
		return myEndTime;
	}

	public void setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
	}

	public String getReport() {
		return myReport;
	}

	public void setReport(String theReport) {
		myReport = theReport;
	}

	public String getErrorMsg() {
		return myErrorMsg;
	}

	public void setErrorMsg(String theErrorMsg) {
		myErrorMsg = theErrorMsg;
	}
}
