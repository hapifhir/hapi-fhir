package ca.uhn.fhir.jpa.bulk;

import org.hl7.fhir.instance.model.api.IIdType;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IBulkDataExportSvc {
	void buildExportFiles();

	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	JobInfo submitJob(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters);

	JobInfo getJobStatus(String theJobId);

	void cancelAndPurgeAllJobs();

	class JobInfo {
		private String myJobId;
		private BulkJobStatusEnum myStatus;
		private List<FileEntry> myFiles;

		public String getJobId() {
			return myJobId;
		}

		public JobInfo setJobId(String theJobId) {
			myJobId = theJobId;
			return this;
		}

		public List<FileEntry> getFiles() {
			return myFiles;
		}

		public void setFiles(List<FileEntry> theFiles) {
			myFiles = theFiles;
		}

		public BulkJobStatusEnum getStatus() {
			return myStatus;
		}

		public void setStatus(BulkJobStatusEnum theStatus) {
			myStatus = theStatus;
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
