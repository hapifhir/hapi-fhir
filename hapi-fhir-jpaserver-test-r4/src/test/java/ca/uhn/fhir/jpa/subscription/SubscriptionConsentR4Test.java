package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests ConsentInterceptor behavior with Subscriptions enabled.
 */
@Import(SubscriptionProcessorConfig.class)
public class SubscriptionConsentR4Test extends BaseSubscriptionsR4Test {
	private final TestConsentService myConsentService = new TestConsentService();
	private final ConsentInterceptor myConsentInterceptor = new ConsentInterceptor(myConsentService);

	@BeforeEach
	public void beforeRegisterInterceptor() {
		myInterceptorRegistry.registerInterceptor(myConsentInterceptor);
	}

	@AfterEach
	public void afterUnregisterInterceptor() {
		myInterceptorRegistry.unregisterInterceptor(myConsentInterceptor);
	}

	@Test
	public void testCreateSubscription_throughFhirClient_skipsConsent() throws Exception {
		String payload = "application/fhir+json";
		String criteria = "Patient?";

		Subscription subscription = createSubscription(criteria, payload);

		assertSubscriptionNotProcessedByConsent(subscription.getIdElement());

		Patient patient = sendPatient();

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourPatientProvider.getCountCreate());
		ourPatientProvider.waitForUpdateCount(1);

		assertThat(myConsentService.isProcessedByConsent(patient.getIdElement())).isTrue();
	}

	@Test
	public void testCreateSubscription_throughDao_skipsConsent() throws Exception {
		String payload = "application/fhir+json";
		String criteria = "Patient?";
		Subscription subscription = FhirR4Util.createSubscription(criteria, payload, myServerBase, null);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		ConsentInterceptor.skipAllConsentForRequest(systemRequestDetails);
		IIdType subscriptionId = mySubscriptionDao.create(subscription, systemRequestDetails).getId();

		assertSubscriptionNotProcessedByConsent(subscriptionId);
	}

	private void assertSubscriptionNotProcessedByConsent(IIdType theSubscriptionId) throws Exception {
		assertThat(myConsentService.isProcessedByConsent(theSubscriptionId)).isFalse();

		waitForActivatedSubscriptionCount(1);

		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		ConsentInterceptor.skipAllConsentForRequest(systemRequestDetails);
		Subscription subscription = mySubscriptionDao.read(theSubscriptionId, systemRequestDetails);
		assertThat(myConsentService.isProcessedByConsent(subscription.getIdElement())).isFalse();
	}

	static class TestConsentService implements IConsentService {
		private final Collection<IIdType> myResourceIdsProcessedByConsent = new HashSet<>();
		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			myResourceIdsProcessedByConsent.add(theResource.getIdElement());
			return IConsentService.super.canSeeResource(theRequestDetails, theResource, theContextServices);
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			myResourceIdsProcessedByConsent.add(theResource.getIdElement());
			return IConsentService.super.willSeeResource(theRequestDetails, theResource, theContextServices);
		}

		public boolean isProcessedByConsent(IIdType theId) {
			return myResourceIdsProcessedByConsent.contains(theId);
		}
	}

}
