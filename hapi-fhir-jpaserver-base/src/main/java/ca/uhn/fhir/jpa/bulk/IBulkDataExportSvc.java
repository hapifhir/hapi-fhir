package ca.uhn.fhir.jpa.bulk;

import org.hl7.fhir.r4.model.DateTimeType;

import java.util.Set;

public interface IBulkDataExportSvc {
	void runPass();

	NewJobInfo submitJob(Set<String> theResourceTypes, DateTimeType theSince, Set<String> theFilters);

	void cancelAndPurgeAllJobs();

	class NewJobInfo {
		private String myJobId;

		public NewJobInfo setJobId(String theJobId) {
			myJobId = theJobId;
			return this;
		}
	}
}
