package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import org.hl7.fhir.r4b.model.CanonicalType;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.Subscription;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SubscriptionValidatingInterceptorTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionValidatingInterceptorTest.class);
	public static final String TEST_SUBSCRIPTION_TOPIC_URL = "http://test.topic";

	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@MockBean
	private JpaStorageSettings myStorageSettings;
	@MockBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;

	@BeforeEach
	public void before() {
		when(myDaoRegistry.isResourceTypeSupported(any())).thenReturn(true);
	}

	@Test
	public void testEmptySub() {
		try {
			Subscription badSub = new Subscription();
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
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
			badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(11) + "Subscription.criteria must be populated"));
		}
	}

	@Test
	public void testBadCriteria() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient");
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(14) + "Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}
	}

	@Test
	public void testBadChannel() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient?");
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(20) + "Subscription.channel.type must be populated"));
		}
	}

	@Test
	public void testEmptyEndpoint() {
		try {
			Subscription badSub = new Subscription();
			badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
			badSub.setCriteria("Patient?");
			Subscription.SubscriptionChannelComponent channel = badSub.getChannel();
			channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(16) + "No endpoint defined for message subscription"));
		}
	}

	@Test
	public void testMalformedEndpoint() {
		Subscription badSub = new Subscription();
		badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
		badSub.setCriteria("Patient?");
		Subscription.SubscriptionChannelComponent channel = badSub.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);

		channel.setEndpoint("foo");
		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel");
		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'"));
		}

		channel.setEndpoint("channel:");
		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(19) + "Invalid subscription endpoint uri channel:"));
		}

		// Happy path
		channel.setEndpoint("channel:my-queue-name");
		mySubscriptionValidatingInterceptor.resourcePreCreate(badSub, null, null);
	}

	@Test
	public void testSubscriptionUpdate() {
		final Subscription subscription = createSubscription();

		// Assert there is no Exception thrown here.
		mySubscriptionValidatingInterceptor.resourceUpdated(subscription, subscription, null, null);
	}

	@Test
	public void testInvalidPointcut() {
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(createSubscription(), null, null, Pointcut.TEST_RB);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(2267) + "Expected Pointcut to be either STORAGE_PRESTORAGE_RESOURCE_CREATED or STORAGE_PRESTORAGE_RESOURCE_UPDATED but was: " + Pointcut.TEST_RB));
		}
	}

	@Test
	public void testInvalidTopic() throws URISyntaxException {
		when(myDaoRegistry.getResourceDao("SubscriptionTopic")).thenReturn(mySubscriptionTopicDao);

		SimpleBundleProvider emptyBundleProvider = new SimpleBundleProvider(Collections.emptyList());
		when(mySubscriptionTopicDao.search(any(), any())).thenReturn(emptyBundleProvider);

		org.hl7.fhir.r4b.model.Subscription badSub = new org.hl7.fhir.r4b.model.Subscription();
		badSub.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
		badSub.getMeta().getProfile().add(new CanonicalType(new URI("http://other.profile")));
		badSub.getMeta().getProfile().add(new CanonicalType(new URI(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL)));
		badSub.setCriteria("http://topic.url");
		Subscription.SubscriptionChannelComponent channel = badSub.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:my-queue-name");
		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub, null, null, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), is(Msg.code(2322) + "No SubscriptionTopic exists with topic: http://topic.url"));
		}

		// Happy path
		SubscriptionTopic topic = new SubscriptionTopic();
		SimpleBundleProvider simpleBundleProvider = new SimpleBundleProvider(List.of(topic));
		when(mySubscriptionTopicDao.search(any(), any())).thenReturn(simpleBundleProvider);
		mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub, null, null, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);
	}

	@Configuration
	public static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR4B();
		}

		@Bean
		SubscriptionValidatingInterceptor subscriptionValidatingInterceptor() {
			return new SubscriptionValidatingInterceptor();
		}

		@Bean
		SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext) {
			return new SubscriptionCanonicalizer(theFhirContext);
		}

		@Bean
		SubscriptionQueryValidator subscriptionQueryValidator(DaoRegistry theDaoRegistry, SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
			return new SubscriptionQueryValidator(theDaoRegistry, theSubscriptionStrategyEvaluator);
		}
	}

	@Nonnull
	private static Subscription createSubscription() {
		final Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatus.REQUESTED);
		subscription.setCriteria("Patient?");
		final Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setEndpoint("channel");
		return subscription;
	}
}
