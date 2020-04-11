package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.entity.PartitionId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestTenantSelectingInterceptor {

	@Autowired
	private IPartitionConfigSvc myPartitionConfigSvc;
	@Autowired
	private FhirContext myFhirContext;

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public PartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public PartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@NotNull
	private PartitionId extractPartitionIdFromRequest(ServletRequestDetails theRequestDetails) {
		String tenantId = theRequestDetails.getTenantId();

		PartitionEntity partition;
		try {
			partition = myPartitionConfigSvc.getPartitionByName(tenantId);
		} catch (IllegalArgumentException e) {
			String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestTenantSelectingInterceptor.class, "unknownTenantName", tenantId);
			throw new ResourceNotFoundException(msg);
		}

		PartitionId retVal = new PartitionId();
		retVal.setPartitionId(partition.getId());
		return retVal;
	}


}
