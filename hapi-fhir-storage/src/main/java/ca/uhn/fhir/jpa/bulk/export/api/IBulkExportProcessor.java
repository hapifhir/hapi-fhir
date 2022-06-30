package ca.uhn.fhir.jpa.bulk.export.api;

import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

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
	 * Does the MDM expansion of resources if necessary
	 * @param theResources - the list of resources to expand
	 */
	void expandMdmResources(List<IBaseResource> theResources);
}
