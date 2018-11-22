package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceLinkResolver {
	ResourceTable findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, String theId);
}
