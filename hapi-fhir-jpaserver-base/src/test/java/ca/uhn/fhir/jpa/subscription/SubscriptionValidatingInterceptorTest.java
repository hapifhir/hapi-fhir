package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionValidatingInterceptorTest {

	@Mock
	public DaoRegistry myDaoRegistry;
	private SubscriptionValidatingInterceptor mySvc;
	private FhirContext myCtx = FhirContext.forCached(FhirVersionEnum.R4);
	@Mock
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	@BeforeEach
	public void before() {
		mySvc = new SubscriptionValidatingInterceptor();
		mySvc.setSubscriptionCanonicalizerForUnitTest(new SubscriptionCanonicalizer(myCtx));
		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);
		mySvc.setSubscriptionStrategyEvaluatorForUnitTest(mySubscriptionStrategyEvaluator);
		mySvc.setFhirContextForUnitTest(myCtx);
	}

	@Test
	public void testValidate_Empty() {
		Subscription subscription = new Subscription();

		try {
			mySvc.validateSubmittedSubscription(subscription);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.status must be populated on this server"));
		}
	}

	@Test
	public void testValidate_RestHook_Populated() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setPayload("application/fhir+json");
		subscription.getChannel().setEndpoint("http://foo");

		mySvc.validateSubmittedSubscription(subscription);
	}

	@Test
	public void testValidate_RestHook_ResourceTypeNotSupported() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(false);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setPayload("application/fhir+json");
		subscription.getChannel().setEndpoint("http://foo");

		try {
			mySvc.validateSubmittedSubscription(subscription);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria contains invalid/unsupported resource type: Patient"));
		}
	}

	@Test
	public void testValidate_RestHook_NoEndpoint() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setPayload("application/fhir+json");

		try {
			mySvc.validateSubmittedSubscription(subscription);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Rest-hook subscriptions must have Subscription.channel.endpoint defined"));
		}
	}


	@Test
	public void testValidate_RestHook_NoType() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setPayload("application/fhir+json");
		subscription.getChannel().setEndpoint("http://foo");

		try {
			mySvc.validateSubmittedSubscription(subscription);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.channel.type must be populated"));
		}
	}

	@Test
	public void testValidate_RestHook_NoPayload() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint("http://foo");

		mySvc.validateSubmittedSubscription(subscription);
	}

	@Test
	public void testValidate_RestHook_NoCriteria() {
		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setPayload("application/fhir+json");
		subscription.getChannel().setEndpoint("http://foo");

		try {
			mySvc.validateSubmittedSubscription(subscription);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria must be populated"));
		}
	}

}
