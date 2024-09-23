package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class PartitionSelectorInterceptor {
	private Integer myNextPartitionId;

	public void setNextPartitionId(Integer theNextPartitionId) {
		myNextPartitionId = theNextPartitionId;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
		String resourceType = FhirContext.forR5Cached().getResourceType(theResource);
		return selectPartition(resourceType);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theDetails) {
		return selectPartition(theDetails.getResourceType());
	}

	@Nonnull
	private RequestPartitionId selectPartition(String theResourceType) {
		return switch (theResourceType) {
			case "Patient", "Observation", "Encounter" -> {
				assert myNextPartitionId != null;
				yield RequestPartitionId.fromPartitionId(myNextPartitionId);
			}
			case "SearchParameter", "Organization" -> RequestPartitionId.defaultPartition();
			default -> throw new InternalErrorException("Don't know how to handle resource type: " + theResourceType);
		};
	}

}
