package ca.uhn.fhir.jpa.bulk;

import org.hl7.fhir.instance.model.api.IIdType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IBulkDataExportSvc {
	void buildExportFiles();

	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	JobInfo submitJob(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters);

	JobInfo getJobStatusOrThrowResourceNotFound(String theJobId);

	void cancelAndPurgeAllJobs();

	class JobInfo {
		private String myJobId;
		private BulkJobStatusEnum myStatus;
		private List<FileEntry> myFiles;
		private String myRequest;
		private Date myStatusTime;
		private String myStatusMessage;

		public String getRequest() {
			return myRequest;
		}

		public void setRequest(String theRequest) {
			myRequest = theRequest;
		}

		public Date getStatusTime() {
			return myStatusTime;
		}

		public JobInfo setStatusTime(Date theStatusTime) {
			myStatusTime = theStatusTime;
			return this;
		}

		public String getJobId() {
			return myJobId;
		}

		public JobInfo setJobId(String theJobId) {
			myJobId = theJobId;
			return this;
		}

		public List<FileEntry> getFiles() {
			if (myFiles == null) {
				myFiles = new ArrayList<>();
			}
			return myFiles;
		}

		public BulkJobStatusEnum getStatus() {
			return myStatus;
		}

		public JobInfo setStatus(BulkJobStatusEnum theStatus) {
			myStatus = theStatus;
			return this;
		}

		public String getStatusMessage() {
			return myStatusMessage;
		}

		public JobInfo setStatusMessage(String theStatusMessage) {
			myStatusMessage = theStatusMessage;
			return this;
		}

		public FileEntry addFile() {
			FileEntry retVal = new FileEntry();
			getFiles().add(retVal);
			return retVal;
		}
	}


	class FileEntry {
		private String myResourceType;
		private IIdType myResourceId;

		public String getResourceType() {
			return myResourceType;
		}

		public FileEntry setResourceType(String theResourceType) {
			myResourceType = theResourceType;
			return this;
		}

		public IIdType getResourceId() {
			return myResourceId;
		}

		public FileEntry setResourceId(IIdType theResourceId) {
			myResourceId = theResourceId;
			return this;
		}
	}


}
