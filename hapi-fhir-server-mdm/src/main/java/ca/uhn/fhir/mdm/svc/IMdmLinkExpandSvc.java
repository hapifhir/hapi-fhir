package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public interface IMdmLinkExpandSvc {
	Set<String> expandMdmBySourceResource(IBaseResource theResource);

	Set<String> expandMdmBySourceResourceId(IIdType theId);

	Set<String> expandMdmBySourceResourcePid(Long theSourceResourcePid);

	Set<String> expandMdmByGoldenResourceId(Long theGoldenResourcePid);

	Set<String> expandMdmByGoldenResourcePid(Long theGoldenResourcePid);

	Set<String> expandMdmByGoldenResourceId(IdDt theId);
}
