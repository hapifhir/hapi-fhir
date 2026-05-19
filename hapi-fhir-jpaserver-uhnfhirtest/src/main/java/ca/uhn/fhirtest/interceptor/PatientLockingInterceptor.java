package ca.uhn.fhirtest.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Patient compartments managed by Mark Braunstein
 */
public class PatientLockingInterceptor {

	private final Set<String> myLockedPatients = Set.of(
		"Patient/131707439",
		"Patient/131896579"
	);

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	void update(RequestDetails theRequestDetails, IBaseResource theOldResource, IBaseResource theNewResource) {
		validateNotLocked(theRequestDetails, theOldResource);
		validateNotLocked(theRequestDetails, theNewResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	void update(RequestDetails theRequestDetails, IBaseResource theResource) {
		validateNotLocked(theRequestDetails, theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	void delete(RequestDetails theRequestDetails, IBaseResource theResource) {
		validateNotLocked(theRequestDetails, theResource);
	}

	private void validateNotLocked(RequestDetails theRequestDetails, IBaseResource resource) {
		if (theRequestDetails != null) {
			String resourceId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			ValidateUtil.isTrueOrThrowInvalidRequest(!myLockedPatients.contains(resourceId), "Resource is locked: %s", resourceId);

			FhirContext ctx = theRequestDetails.getFhirContext();
			List<String> owners = ctx.newTerser().getCompartmentOwnersForResource("Patient", resource, Set.of())
				.stream()
				.map(t->t.toUnqualifiedVersionless().getValue())
				.toList();

			Collection<String> intersection = CollectionUtils.intersection(owners, myLockedPatients);
			ValidateUtil.isTrueOrThrowInvalidRequest(intersection.isEmpty(), "Compartment is locked: %s", intersection);
		}
	}


}
