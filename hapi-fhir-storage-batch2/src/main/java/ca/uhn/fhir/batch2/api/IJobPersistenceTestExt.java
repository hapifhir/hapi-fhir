package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;

/**
 * some helpers are visible to support our tests
 */
public interface IJobPersistenceTestExt extends IJobPersistence {


	/**
	 * Update the stored instance.
	 *
	 * @param theInstance The instance - Must contain an ID
	 * @return true if the status changed
	 */
	boolean updateInstance(JobInstance theInstance);
}
