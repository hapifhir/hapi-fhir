package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashSet;
import java.util.Set;

public class TestPartitionSelectorInterceptor {
	private RequestPartitionId myNextPartition;
	private final Set<String> myNonPartitionableResources = new HashSet<>();
	private BaseRequestPartitionHelperSvc myHelperSvc = new RequestPartitionHelperSvc();

	/**
	 * Constructor
	 */
	public TestPartitionSelectorInterceptor() {
		super();
	}

	public TestPartitionSelectorInterceptor addNonPartitionableResource(@Nonnull String theResourceName) {
		Validate.notBlank(theResourceName, "Must not be blank");
		myNonPartitionableResources.add(theResourceName);
		return this;
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
		if (theResourceType != null) {
			if (!myHelperSvc.isResourcePartitionable(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}
			if (myNonPartitionableResources.contains(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}
		}

		assert myNextPartition != null;
		return myNextPartition;
	}
}
