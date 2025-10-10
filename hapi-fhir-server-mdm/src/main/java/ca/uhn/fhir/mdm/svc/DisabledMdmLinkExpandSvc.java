package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

// FIXME GGG unsupportedoperation for all of these.
public class DisabledMdmLinkExpandSvc implements IMdmLinkExpandSvc {
	@Override
	public Set<String> expandMdmBySourceResource(RequestPartitionId theRequestPartitionId, IBaseResource theResource) {
		return Set.of();
	}

	@Override
	public Set<String> expandMdmBySourceResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		return Set.of();
	}

	@Override
	public Set<String> expandMdmBySourceResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theSourceResourcePid) {
		return Set.of();
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		return Set.of();
	}

	@Override
	public Set<String> expandMdmByGoldenResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		return Set.of();
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		return Set.of();
	}

	@Override
	public Set<JpaPid> expandGroup(String groupResourceId, RequestPartitionId requestPartitionId) {
		return Set.of();
	}

	@Override
	public void annotateResource(IBaseResource resource) {}
}
