package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public interface IMdmResourceDaoSvc {
	DaoMethodOutcome upsertGoldenResource(IAnyResource theGoldenResource, String theResourceType);

	/**
	 * Given a resource, remove its Golden Resource tag.
	 *
	 * @param theGoldenResource the {@link IAnyResource} to remove the tag from.
	 * @param theResourcetype   the type of that resource
	 */
	void removeGoldenResourceTag(IAnyResource theGoldenResource, String theResourcetype);

	IAnyResource readGoldenResourceByPid(IResourcePersistentId theGoldenResourcePid, String theResourceType);

	Optional<IAnyResource> searchGoldenResourceByEID(String theEid, String theResourceType);

	Optional<IAnyResource> searchGoldenResourceByEID(
			String theEid, String theResourceType, RequestPartitionId thePartitionId);
}
