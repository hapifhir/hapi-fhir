package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
public class SubscriptionValidatingInterceptorTest {
	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	@BeforeEach
	public void before() {
		when(myDaoRegistry.isResourceTypeSupported(any())).thenReturn(true);
	}

	@Test
	public void testEmptySub() {
		try {
			Subscription badSub = new Subscription();
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Can not process submitted Subscription - Subscription.status must be populated on this server"));
		}
	}

	@Test
	public void testEmptyStatus() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Subscription.SubscriptionStatus.ACTIVE);
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Subscription.criteria must be populated"));
		}
	}

	@Test
	public void testBadCriteria() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Subscription.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient");
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}
	}

	@Test
	public void testBadChannel() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Subscription.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient?");
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Subscription.channel.type must be populated"));
		}
	}

	@Test
	public void testEmptyEndpoint() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Subscription.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient?");
			Subscription.SubscriptionChannelComponent channel = badSub.getChannel();
			channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("No endpoint defined for message subscription"));
		}
	}

	@Test
	public void testMalformedEndpoint() {
		Subscription badSub = new Subscription();
		badSub.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		badSub.setCriteria("Patient?");
		Subscription.SubscriptionChannelComponent channel = badSub.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);

		channel.setEndpoint("foo");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel:");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is("Invalid subscription endpoint uri channel:"));
		}

		// Happy path
		channel.setEndpoint("channel:my-queue-name");
		mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
	}

	@Configuration
	public static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean
		SubscriptionValidatingInterceptor subscriptionValidatingInterceptor() {
			return new SubscriptionValidatingInterceptor();
		}

		@Bean
		SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext) {
			return new SubscriptionCanonicalizer(theFhirContext);
		}
	}
}
