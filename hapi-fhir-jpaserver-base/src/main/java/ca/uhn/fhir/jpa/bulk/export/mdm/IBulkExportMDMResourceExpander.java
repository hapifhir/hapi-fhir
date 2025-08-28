package ca.uhn.fhir.jpa.bulk.export.mdm;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Set;

public interface IBulkExportMDMResourceExpander {
	Set<JpaPid> expandGroup(String groupResourceId, SystemRequestDetails requestDetails);

	void annotateResource(IBaseResource resource);
}
