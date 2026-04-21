package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Tests ConsentInterceptor behavior with Subscriptions enabled.
 */
@Import(SubscriptionProcessorConfig.class)
public class SubscriptionConsentR4Test extends BaseSubscriptionsR4Test {

	@Nested
	class WithRejectingConsentInterceptorTest {
		private final RejectingConsentService myConsentService = new RejectingConsentService();
		private final ConsentInterceptor myConsentInterceptor = new ConsentInterceptor(myConsentService) {
			@Override
			protected boolean isSkipServiceForRequest(RequestDetails theRequestDetails) {
				return false;
			}
		};

		@BeforeEach
		void beforeRegisterInterceptor() {
			myInterceptorRegistry.registerInterceptor(myConsentInterceptor);
		}

		@AfterEach
		void afterUnregisterInterceptor() {
			myInterceptorRegistry.unregisterInterceptor(myConsentInterceptor);
		}

		@Test
		@Order(1)
		void testCreateSubscription_throughFhirClient_rejectsReadDuringActivate() throws Exception {
			// test
			try {
				createSubscription();
			} catch (ResourceNotFoundException e) {
				fail("Should be able to create a subscription", e);
			}

			try {
				waitForActivatedSubscriptionCount(1);
				fail("Should not be able to activate a subscription");
			} catch (IllegalStateException e) {
				// ok
			}
		}
	}

	@Nested
	class WithDefaultConsentInterceptorTest {
		private final TestConsentService myConsentService = new TestConsentService();
		private final ConsentInterceptor myConsentInterceptor = new ConsentInterceptor(myConsentService);

		@BeforeEach
		void beforeRegisterInterceptor() {
			myInterceptorRegistry.registerInterceptor(myConsentInterceptor);
		}

		@AfterEach
		void afterUnregisterInterceptor() {
			myInterceptorRegistry.unregisterInterceptor(myConsentInterceptor);
		}

		@Test
		@Order(1)
		void testCreateSubscription_throughFhirClient_isProcessedByConsent() throws Exception {
			// test
			IIdType subscriptionId = createSubscription();

			// verify creation
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isTrue();

			// verify registration and activation
			waitForActivatedSubscriptionCount(1);
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isTrue();
		}

		@Test
		@Order(1)
		void testCreateSubscription_throughDaoWithSkipConsentFlag_isNotProcessedByConsent() throws Exception {
			String payload = "application/fhir+json";
			String criteria = "Patient?";

			// given
			Subscription subscription = FhirR4Util.createSubscription(criteria, payload, myServerBase, null);
			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
			ConsentInterceptor.skipAllConsentForRequest(systemRequestDetails);

			// test
			IIdType subscriptionId = mySubscriptionDao.create(subscription, systemRequestDetails).getId();
			waitForActivatedSubscriptionCount(1);

			// verify
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isFalse();
		}

		@Test
		@Order(1)
		void testCreateSubscription_throughDaoWithoutSkipConsentFlag_isProcessedByConsent() throws Exception {
			String payload = "application/fhir+json";
			String criteria = "Patient?";

			// given
			Subscription subscription = FhirR4Util.createSubscription(criteria, payload, myServerBase, null);
			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();

			// test
			IIdType subscriptionId = mySubscriptionDao.create(subscription, systemRequestDetails).getId();
			waitForActivatedSubscriptionCount(1);

			// verify
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isTrue();
		}

		@Test
		@Order(2)
		void testReadSubscription_throughFhirClient_isProcessedByConsent() throws Exception {
			// given
			IIdType subscriptionId = createSubscription();
			waitForActivatedSubscriptionCount(1);

			// test
			myClient.read().resource("Subscription").withId(subscriptionId).execute();

			// verify
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isTrue();
		}

		@Test
		@Order(2)
		void testReadSubscription_throughDaoWithSkipConsentFlag_isNotProcessedByConsent() throws Exception {
			// given
			IIdType subscriptionId = createSubscription();
			waitForActivatedSubscriptionCount(1);
			myConsentService.reset();

			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
			ConsentInterceptor.skipAllConsentForRequest(systemRequestDetails);

			// test
			mySubscriptionDao.read(subscriptionId, systemRequestDetails);

			// verify
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isFalse();
		}

		@Test
		@Order(2)
		void testReadSubscription_throughDaoWithoutSkipConsentFlag_processedByConsent() throws Exception {
			// given
			IIdType subscriptionId = createSubscription();
			waitForActivatedSubscriptionCount(1);
			myConsentService.reset();

			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();

			// test
			mySubscriptionDao.read(subscriptionId, systemRequestDetails);

			// verify
			assertThat(myConsentService.isProcessedByConsent(subscriptionId)).isTrue();
		}
	}

	IIdType createSubscription() {
		String payload = "application/fhir+json";
		String criteria = "Patient?";

		Subscription subscription = createSubscription(criteria, payload);
		return subscription.getIdElement();
	}

	static class TestConsentService implements IConsentService {
		private final Collection<IIdType> myResourceIdsProcessedByConsent = new HashSet<>();
		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			myResourceIdsProcessedByConsent.add(theResource.getIdElement().toUnqualifiedVersionless());
			return IConsentService.super.canSeeResource(theRequestDetails, theResource, theContextServices);
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			myResourceIdsProcessedByConsent.add(theResource.getIdElement().toUnqualifiedVersionless());
			return IConsentService.super.willSeeResource(theRequestDetails, theResource, theContextServices);
		}

		boolean isProcessedByConsent(IIdType theId) {
			return myResourceIdsProcessedByConsent.contains(theId.toUnqualifiedVersionless());
		}

		void reset() {
			myResourceIdsProcessedByConsent.clear();
		}
	}

	static class RejectingConsentService implements IConsentService {
		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.REJECT;
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.REJECT;
		}
	}

}
