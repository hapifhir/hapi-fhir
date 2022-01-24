package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SubscriptionValidatingInterceptorTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionValidatingInterceptorTest.class);

	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@MockBean
	private DaoConfig myDaoConfig;
	@MockBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

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
			assertThat(e.getMessage(), is(Msg.code(8) + "Can not process submitted Subscription - Subscription.status must be populated on this server"));
			ourLog.info("Expected exception", e);
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
			assertThat(e.getMessage(), is(Msg.code(11) + "Subscription.criteria must be populated"));
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
			assertThat(e.getMessage(), is(Msg.code(14) + "Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
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
			assertThat(e.getMessage(), is(Msg.code(20) + "Subscription.channel.type must be populated"));
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
			assertThat(e.getMessage(), is(Msg.code(16) + "No endpoint defined for message subscription"));
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
			assertThat(e.getMessage(), is(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel:");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(19) + "Invalid subscription endpoint uri channel:"));
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
