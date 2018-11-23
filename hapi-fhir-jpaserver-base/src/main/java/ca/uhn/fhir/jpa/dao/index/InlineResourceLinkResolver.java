package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

@Service
public class InlineResourceLinkResolver implements IResourceLinkResolver {

	@Override
	public ResourceTable findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, String theId) {
		ResourceTable target;
		target = new ResourceTable();
		target.setResourceType(theTypeString);
		if (theNextId.isIdPartValidLong()) {
			target.setId(theNextId.getIdPartAsLong());
		} else {
			ForcedId forcedId = new ForcedId();
			forcedId.setForcedId(theId);
			target.setForcedId(forcedId);
		}
		return target;
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		// When resolving reference in-memory for a single resource, there's nothing to validate
	}
}
