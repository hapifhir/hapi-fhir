package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ResourceTableHelper {
	@Autowired
	private IdHelperService myIdHelperService;

	@Nonnull
	Long getPidOrThrowException(IIdType theId) {
		List<ResourcePersistentId> resourcePersistentIds = myIdHelperService.resolveResourcePersistentIds(Collections.singletonList(theId), null);
		return resourcePersistentIds.get(0).getIdAsLong();
	}
}
