package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.IChannelTypeValidator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RegexEndpointUrlValidationStrategy;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RestHookChannelValidator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.SubscriptionChannelTypeValidatorFactory;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.SubscriptionQueryValidator;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.CanonicalType;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.Subscription;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
	private SubscriptionSettings mySubscriptionSettings;
	@MockBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;
	private FhirContext myFhirContext;

	@SpyBean
	private SubscriptionChannelTypeValidatorFactory mySubscriptionChannelTypeValidatorFactory;

	@BeforeEach
	public void before() {
		setFhirContext(FhirVersionEnum.R4B);
		when(myDaoRegistry.isResourceTypeSupported(any())).thenReturn(true);
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion345")
	public void testEmptySub(IBaseResource theSubscription) {
		setFhirContext(theSubscription);

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(8) + "Can not process submitted Subscription - Subscription.status must be populated on this server", e.getMessage());
			ourLog.info("Expected exception", e);
		}
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion34") // R5 subscriptions don't have criteria
	public void testEmptyCriteria(IBaseResource theSubscription) {
		initSubscription(theSubscription);

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).startsWith(Msg.code(11) + "Subscription.");
			assertThat(e.getMessage()).endsWith( " must be populated");
		}
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion34")
	public void testBadCriteria(IBaseResource theSubscription) {
		initSubscription(theSubscription);
		SubscriptionUtil.setCriteria(myFhirContext, theSubscription, "Patient");

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).endsWith("criteria must be in the form \"{Resource Type}?[params]\"");
		}
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion34")
	public void testBadChannel(IBaseResource theSubscription) {
		initSubscription(theSubscription);
		SubscriptionUtil.setCriteria(myFhirContext, theSubscription, "Patient?");

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(20) + "Subscription.channel.type must be populated", e.getMessage());
		}
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion345")
	public void testEmptyEndpoint(IBaseResource theSubscription) {
		initSubscription(theSubscription);
		SubscriptionUtil.setCriteria(myFhirContext, theSubscription, "Patient?");
		SubscriptionUtil.setChannelType(myFhirContext, theSubscription, "message");

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(16) + "No endpoint defined for message subscription", e.getMessage());
		}
	}

	@ParameterizedTest
	@MethodSource("subscriptionByFhirVersion345")
	public void testMalformedEndpoint(IBaseResource theSubscription) {
		initSubscription(theSubscription);
		SubscriptionUtil.setCriteria(myFhirContext, theSubscription, "Patient?");
		SubscriptionUtil.setChannelType(myFhirContext, theSubscription, "message");
		SubscriptionUtil.setEndpoint(myFhirContext, theSubscription, "foo");

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'", e.getMessage());
		}

		SubscriptionUtil.setEndpoint(myFhirContext, theSubscription, "channel");
		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'", e.getMessage());
		}

		SubscriptionUtil.setEndpoint(myFhirContext, theSubscription, "channel:");
		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(19) + "Invalid subscription endpoint uri channel:", e.getMessage());
		}

		// Happy path
		SubscriptionUtil.setEndpoint(myFhirContext, theSubscription, "channel:my-queue-name");
		mySubscriptionValidatingInterceptor.resourcePreCreate(theSubscription, null, null);
	}

	@Test
	public void testCreateSubscription_whenCreatedOnNonDefaultPartition_willFail() {
		final Subscription subscription = createSubscription();
		ExtensionUtil.addExtension(myFhirContext, subscription, HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, "boolean", Boolean.TRUE);

		when(mySubscriptionSettings.isCrossPartitionSubscriptionEnabled()).thenReturn(true);
		when(myRequestPartitionHelperSvc.isDefaultPartition(any())).thenReturn(false);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		try {
			mySubscriptionValidatingInterceptor.resourcePreCreate(subscription, null, requestPartitionId);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(2010) + "Cross partition subscription must be created on the default partition", e.getMessage());
		}
	}

	@Test
	public void testSubscriptionUpdate() {
		final Subscription subscription = createSubscription();

		assertThatNoException().isThrownBy(() -> mySubscriptionValidatingInterceptor.resourceUpdated(subscription, subscription, null, null));
	}

	@Test
	public void testInvalidPointcut() {
		final Subscription subscription = createSubscription();

		try {
			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(subscription, null, null, Pointcut.TEST_RB);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(2267) + "Expected Pointcut to be either STORAGE_PRESTORAGE_RESOURCE_CREATED or STORAGE_PRESTORAGE_RESOURCE_UPDATED but was: " + Pointcut.TEST_RB, e.getMessage());
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
			fail("");
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(2322) + "No SubscriptionTopic exists with topic: http://topic.url", e.getMessage());
		}

		// Happy path
		SubscriptionTopic topic = new SubscriptionTopic();
		SimpleBundleProvider simpleBundleProvider = new SimpleBundleProvider(List.of(topic));
		when(mySubscriptionTopicDao.search(any(), any())).thenReturn(simpleBundleProvider);
		mySubscriptionValidatingInterceptor.validateSubmittedSubscription(badSub, null, null, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);

		verify(mySubscriptionChannelTypeValidatorFactory, times(1)).getValidatorForChannelType(CanonicalSubscriptionChannelType.MESSAGE);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"acme.corp",
		"https://acme.corp/badstuff-%%$^&& iuyi",
		"ftp://acme.corp"})
	public void testRestHookEndpointValidation_whenProvidedWithBadURLs(String theBadUrl) {
		try {
			Subscription subscriptionWithBadEndpoint = createSubscription();
			subscriptionWithBadEndpoint.getChannel().setEndpoint(theBadUrl);

			mySubscriptionValidatingInterceptor.validateSubmittedSubscription(subscriptionWithBadEndpoint, null, null, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);
			fail("");
		} catch (Exception e) {
			verify(mySubscriptionChannelTypeValidatorFactory, times(1)).getValidatorForChannelType(CanonicalSubscriptionChannelType.RESTHOOK);
			assertThat(e.getMessage()).startsWith(Msg.code(2545));
		}
	}

	private void initSubscription(IBaseResource theSubscription) {
		setFhirContext(theSubscription);
		SubscriptionUtil.setStatus(myFhirContext, theSubscription, "active");
		if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			initR5();
			org.hl7.fhir.r5.model.Subscription subscription = (org.hl7.fhir.r5.model.Subscription) theSubscription;
			subscription.setTopic(TEST_SUBSCRIPTION_TOPIC_URL);
		}
	}

	void initR5() {
		when(myDaoRegistry.getResourceDao("SubscriptionTopic")).thenReturn(mySubscriptionTopicDao);
		org.hl7.fhir.r5.model.SubscriptionTopic topic = new org.hl7.fhir.r5.model.SubscriptionTopic();
		IBundleProvider provider = new SimpleBundleProvider(topic);
		when(mySubscriptionTopicDao.search(any(SearchParameterMap.class), any(RequestDetails.class))).thenReturn(provider);
	}

	public static Stream<IBaseResource> subscriptionByFhirVersion345() {
		return subscriptionByFhirVersion(true);
	}

	public static Stream<IBaseResource> subscriptionByFhirVersion34() {
		return subscriptionByFhirVersion(false);
	}

	private void setFhirContext(IBaseResource theSubscription) {
		FhirVersionEnum fhirVersion = theSubscription.getStructureFhirVersionEnum();
		setFhirContext(fhirVersion);
	}

	private void setFhirContext(FhirVersionEnum fhirVersion) {
		myFhirContext = FhirContext.forCached(fhirVersion);
		mySubscriptionValidatingInterceptor.setFhirContext(myFhirContext);
		mySubscriptionValidatingInterceptor.setSubscriptionCanonicalizerForUnitTest(new SubscriptionCanonicalizer(myFhirContext, mySubscriptionSettings));
	}

	private static @Nonnull Stream<IBaseResource> subscriptionByFhirVersion(boolean theIncludeR5) {
		List<IBaseResource> resources = new ArrayList<>();
		resources.add(new org.hl7.fhir.dstu3.model.Subscription());
		resources.add(new org.hl7.fhir.r4.model.Subscription());
		org.hl7.fhir.r4.model.Subscription r4Backport = new org.hl7.fhir.r4.model.Subscription();
		r4Backport.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		resources.add(r4Backport);
		resources.add(new org.hl7.fhir.r4b.model.Subscription());
		if (theIncludeR5) {
			resources.add(new org.hl7.fhir.r5.model.Subscription());
		}

		return resources.stream();
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
			return new SubscriptionCanonicalizer(theFhirContext, new SubscriptionSettings());
		}

		@Bean
        SubscriptionQueryValidator subscriptionQueryValidator(DaoRegistry theDaoRegistry, SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
			return new SubscriptionQueryValidator(theDaoRegistry, theSubscriptionStrategyEvaluator);
		}

		@Bean
		public IChannelTypeValidator restHookChannelValidator() {
			String regex = new SubscriptionSettings().getRestHookEndpointUrlValidationRegex();
			RegexEndpointUrlValidationStrategy regexEndpointUrlValidationStrategy = new RegexEndpointUrlValidationStrategy(regex);
			return new RestHookChannelValidator(regexEndpointUrlValidationStrategy);
		}

		@Bean
		public SubscriptionChannelTypeValidatorFactory subscriptionChannelTypeValidatorFactory(
			List<IChannelTypeValidator> theValidorList) {
			return new SubscriptionChannelTypeValidatorFactory(theValidorList);
		}
	}

	@Nonnull
	private static Subscription createSubscription() {
		final Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatus.REQUESTED);
		subscription.setCriteria("Patient?");
		final Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setEndpoint("http://acme.corp/");
		return subscription;
	}
}
