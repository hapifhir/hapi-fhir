package ca.uhn.fhir.jpa.dao.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.PartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;

import static ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster.doCallHooksAndReturnObject;

public class RequestPartitionHelperService {

	private final HashSet<Object> myPartitioningBlacklist;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private FhirContext myFhirContext;

	public RequestPartitionHelperService() {
		// FIXME: document this list

		myPartitioningBlacklist = new HashSet<>();

		// Infrastructure
		myPartitioningBlacklist.add("Subscription");
		myPartitioningBlacklist.add("SearchParameter");

		// Validation
		myPartitioningBlacklist.add("StructureDefinition");
		myPartitioningBlacklist.add("Questionnaire");

		// Terminology
		myPartitioningBlacklist.add("ConceptMap");
		myPartitioningBlacklist.add("CodeSystem");
		myPartitioningBlacklist.add("ValueSet");
	}

	/**
	 * Invoke the <code>STORAGE_PARTITION_IDENTIFY_READ</code> interceptor pointcut to determine the tenant for a read request
	 */
	@Nullable
	public PartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType) {
		PartitionId partitionId = null;

		if (myDaoConfig.isPartitioningEnabled()) {
			// Interceptor call: STORAGE_PARTITION_IDENTIFY_READ
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			partitionId = (PartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_READ, params);

			validatePartition(partitionId, theResourceType);
		}

		return partitionId;
	}

	/**
	 * Invoke the <code>STORAGE_PARTITION_IDENTIFY_CREATE</code> interceptor pointcut to determine the tenant for a read request
	 */
	@Nullable
	public PartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource) {

		PartitionId partitionId = null;
		if (myDaoConfig.isPartitioningEnabled()) {
			// Interceptor call: STORAGE_PARTITION_IDENTIFY_CREATE
			HookParams params = new HookParams()
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			partitionId = (PartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE, params);

			String resourceName = myFhirContext.getResourceDefinition(theResource).getName();
			validatePartition(partitionId, resourceName);
		}

		return partitionId;
	}

	private void validatePartition(@Nullable PartitionId thePartitionId, @Nonnull String theResourceName) {
		if (thePartitionId != null) {
			if (myPartitioningBlacklist.contains(theResourceName)) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestPartitionHelperService.class, "blacklistedResourceTypeForPartitioning", theResourceName);
				throw new InvalidRequestException(msg);
			}
		}
	}
}
