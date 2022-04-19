package ca.uhn.fhir.jpa.bulk.export.api;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Iterator;

public interface IBulkExportProcessor {

	/**
	 * For fetching PIDs of resources
	 * @param theParams
	 * @return
	 */
	Iterator<ResourcePersistentId> getResourcePidIterator(ExportPIDIteratorParameters theParams);

	/**
	 * Sets the job status
	 * @param theJobId
	 * @param theStatus
	 */
	void setJobStatus(String theJobId, BulkExportJobStatusEnum theStatus);

	/**
	 * Returns the current job status.
	 * @param theJobId
	 * @return
	 */
	BulkExportJobStatusEnum getJobStatus(String theJobId);

	/**
	 * Adds binary to job for resource type
	 * @param theJobId
	 * @param theBinaryId
	 */
	void addFileToCollection(String theJobId, String theResourceType, IIdType theBinaryId);
}
