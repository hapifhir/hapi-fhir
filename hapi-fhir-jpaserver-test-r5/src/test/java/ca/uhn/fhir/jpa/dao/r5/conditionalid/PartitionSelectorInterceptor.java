package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class PartitionSelectorInterceptor {
	private RequestPartitionId myNextPartition;

	/**
	 * Constructor
	 */
	public PartitionSelectorInterceptor() {
		super();
	}

	public void setNextPartitionId(Integer theNextPartitionId) {
		myNextPartition = RequestPartitionId.fromPartitionId(theNextPartitionId);
	}

	public void setNextPartition(RequestPartitionId theNextPartition) {
		myNextPartition = theNextPartition;
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
		return switch (defaultString(theResourceType)) {
			case "", "Patient", "Observation", "Encounter", "List", "QuestionnaireResponse" -> {
				assert myNextPartition != null;
				yield myNextPartition;
			}
			case "SearchParameter", "Organization", "Questionnaire", "CodeSystem", "ValueSet" -> RequestPartitionId.defaultPartition();
			default -> throw new InternalErrorException("Don't know how to handle resource type: " + theResourceType);
		};
	}

}
