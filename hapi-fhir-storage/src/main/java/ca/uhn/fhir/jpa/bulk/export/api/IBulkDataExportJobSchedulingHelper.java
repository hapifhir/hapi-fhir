package ca.uhn.fhir.jpa.bulk.export.api;

import javax.transaction.Transactional;

public interface IBulkDataExportJobSchedulingHelper {

	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	@Transactional(value = Transactional.TxType.NEVER)
	void cancelAndPurgeAllJobs();

	@Transactional(value = Transactional.TxType.NEVER)
	void startSubmittedJobs();
}
