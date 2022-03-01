package ca.uhn.fhir.jpa.bulk.export.api;

import javax.transaction.Transactional;

public interface IBulkDataExportJobSchedulingHelper {

	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();
	void cancelAndPurgeAllJobs();
	void startSubmittedJobs();
}
