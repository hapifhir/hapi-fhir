package ca.uhn.fhir.jpa.bulk.export.api;

import ca.uhn.fhir.jpa.api.model.BulkExportJobInfo;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Iterator;
import java.util.List;

public interface IBulkExportProcessor {

	/**
	 * For fetching PIDs of resources
	 * @param theParams
	 * @return
	 */
	Iterator<ResourcePersistentId> getResourcePidIterator(ExportPIDIteratorParameters theParams);

	/**
	 * Returns the basics of a started (and persisted) job.
	 * @param theJobId - the id of the job
	 * @return - job info
	 */
	BulkExportJobInfo getJobInfo(String theJobId);

	/**
	 * Sets the job status
	 * @param theJobId - the job id
	 * @param theStatus - the status to set
	 * @param theMessage - status message (if any)
	 */
	void setJobStatus(String theJobId, BulkExportJobStatusEnum theStatus, String theMessage);

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

	/**
	 * Does the MDM expansion of resources if necessary
	 * @param theParameters - the bulk job parameters
	 * @param theResources - the list of resources to expand
	 */
	void expandMdmResources(List<IBaseResource> theResources);
}
