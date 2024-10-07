package ca.uhn.fhir.batch2.jobs.reindex.svcs;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ReindexJobStatus;

import java.util.Map;

public class ReindexJobService {

	private final DaoRegistry myDaoRegistry;

	public ReindexJobService(DaoRegistry theRegistry) {
		myDaoRegistry = theRegistry;
	}

	/**
	 * Checks if any of the resource types in the map have any pending reindex work waiting.
	 * This will return true after the first such encounter, and only return false if no
	 * reindex work is required for any resource.
	 * @param theResourceTypesToCheckFlag map of resourceType:whether or not to check
	 * @return true if there's reindex work pending, false otherwise
	 */
	public boolean anyResourceHasPendingReindexWork(Map<String, Boolean> theResourceTypesToCheckFlag) {
		for (String resourceType : theResourceTypesToCheckFlag.keySet()) {
			boolean toCheck = theResourceTypesToCheckFlag.get(resourceType);
			if (toCheck) {
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

				ReindexJobStatus status = dao.getReindexJobStatus();
				if (status.isHasReindexWorkPending()) {
					return true;
				}
			}
		}
		return false;
	}
}
