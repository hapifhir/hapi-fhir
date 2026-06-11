// Created by claude-opus-4-8
package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link ConsentInterceptor#interceptPreAccess} must safely handle the case where
 * {@link IPreResourceAccessDetails#getResource(int)} returns {@code null} for a search PID that has
 * no loadable resource body. The interceptor must not pass that {@code null} into a consent
 * service's {@code canSeeResource}, which production services dereference.
 */
public class ConsentInterceptorPreAccessNullResourceTest {

	/**
	 * A consent service that opts into canSeeResource processing and dereferences the resource the
	 * way production services (e.g. SearchNarrowingConsentService) do — so it NPEs on a null
	 * resource. The interceptor must never invoke canSeeResource with a null resource.
	 */
	private static class DereferencingConsentService implements IConsentService {
		@Override
		public ConsentOutcome canSeeResource(
				RequestDetails theRequestDetails,
				IBaseResource theResource,
				IConsentContextServices theContextServices) {
			// Production consent services inspect the resource (e.g. getFhirContext().getResourceType(theResource)).
			// Touching it directly reproduces the real-world NPE on a null resource.
			theResource.getIdElement();
			return ConsentOutcome.PROCEED;
		}
	}

	@Test
	void interceptPreAccess_whenResourceIsNull_skipsConsentAndMarksDontReturn() {
		// Setup
		DereferencingConsentService theConsentService = spy(new DereferencingConsentService());
		ConsentInterceptor myInterceptor = new ConsentInterceptor(theConsentService);

		// A plain system request: not authorized, not a metadata/$meta path, so the canSeeResource
		// loop in interceptPreAccess actually executes (shouldProcessCanSeeResource defaults to true).
		RequestDetails theRequestDetails = new SystemRequestDetails();

		// Access details with one PID whose resource body could not be loaded (Part-1 behavior: null).
		IPreResourceAccessDetails theAccessDetails = mock(IPreResourceAccessDetails.class);
		when(theAccessDetails.size()).thenReturn(1);
		when(theAccessDetails.getResource(0)).thenReturn(null);

		// Execute + Verify desired Part-2 behavior:
		// the interceptor must not throw on a null resource.
		assertThatCode(() -> myInterceptor.interceptPreAccess(theRequestDetails, theAccessDetails))
				.doesNotThrowAnyException();

		// It must never pass a null resource into a consent service.
		verify(theConsentService, never()).canSeeResource(any(), any(), any());

		// And it must mark the null index so the unloadable resource is not returned.
		verify(theAccessDetails).setDontReturnResourceAtIndex(eq(0));
	}
}
