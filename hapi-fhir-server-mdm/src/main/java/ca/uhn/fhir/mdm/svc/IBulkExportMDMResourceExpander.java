package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Set;

public interface IBulkExportMDMResourceExpander {
	Set<JpaPid> expandGroup(String groupResourceId, RequestPartitionId requestPartitionId);

	void annotateResource(IBaseResource resource);
}
