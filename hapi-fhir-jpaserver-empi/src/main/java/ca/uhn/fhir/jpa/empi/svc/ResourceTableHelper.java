package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class ResourceTableHelper {
	private static final String RESOURCE_PID = "RESOURCE_PID";

	@Autowired
	IdHelperService myIdHelperService;

	@Nullable
	public Long getPidOrNull(IBaseResource theResource) {
		IAnyResource anyResource = (IAnyResource) theResource;
		Long retval = (Long) anyResource.getUserData(RESOURCE_PID);
		if (retval == null) {
			IIdType id = theResource.getIdElement();
			try {
				retval = myIdHelperService.resolveResourcePersistentIds(id.getResourceType(), id.getIdPart()).getIdAsLong();
			} catch (ResourceNotFoundException e) {
				return null;
			}
		}
		return retval;
	}
}
