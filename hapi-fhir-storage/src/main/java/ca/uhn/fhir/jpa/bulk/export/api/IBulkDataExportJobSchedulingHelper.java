package ca.uhn.fhir.jpa.bulk.export.api;

import javax.transaction.Transactional;

public interface IBulkDataExportJobSchedulingHelper {

	/**
	 * invoked via scheduled task, purges any tasks which are past their cutoff point.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	/**
	 * Stops all invoked jobs, and then purges them.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void cancelAndPurgeAllJobs();

	/**
	 * Given all Bulk Export jobs that have been created since the last scheduled run, this method will
	 * start them all. This is invoked primarily via a scheduler.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void startSubmittedJobs();
}
