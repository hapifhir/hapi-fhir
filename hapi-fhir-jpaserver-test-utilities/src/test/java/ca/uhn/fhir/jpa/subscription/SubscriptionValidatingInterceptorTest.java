package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionValidatingInterceptorTest {

	@Mock
	public DaoRegistry myDaoRegistry;
	private SubscriptionValidatingInterceptor mySvc;
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@Mock
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private DaoConfig myDaoConfig;

	@BeforeEach
	public void before() {
		mySvc = new SubscriptionValidatingInterceptor();
		mySvc.setSubscriptionCanonicalizerForUnitTest(new SubscriptionCanonicalizer(myCtx));
		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);
		mySvc.setSubscriptionStrategyEvaluatorForUnitTest(mySubscriptionStrategyEvaluator);
		mySvc.setFhirContextForUnitTest(myCtx);
		mySvc.setDaoConfigForUnitTest(myDaoConfig);
		mySvc.setRequestPartitionHelperSvcForUnitTest(myRequestPartitionHelperSvc);
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
	public void testValidate_RestHook_MultitypeResourceTypeNotSupported() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(false);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("[Patient]");
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

	@Test
	public void testValidate_Cross_Partition_Subscription() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);
		when(myDaoConfig.isCrossPartitionSubscription()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(isA(RequestDetails.class), isA(Subscription.class), eq("Subscription"))).thenReturn(RequestPartitionId.defaultPartition());

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint("http://foo");

		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));

		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRestOperationType(RestOperationTypeEnum.CREATE);

		// No asserts here because the function should throw an UnprocessableEntityException exception if the subscription
		// is invalid
		assertDoesNotThrow(() -> mySvc.validateSubmittedSubscription(subscription, requestDetails));
		Mockito.verify(myDaoConfig, times(1)).isCrossPartitionSubscription();
		Mockito.verify(myDaoRegistry, times(1)).isResourceTypeSupported(eq("Patient"));
		Mockito.verify(myRequestPartitionHelperSvc, times(1)).determineCreatePartitionForRequest(isA(RequestDetails.class), isA(Subscription.class), eq("Subscription"));
	}

	@Test
	public void testValidate_Cross_Partition_Subscription_On_Wrong_Partition() {
		when(myDaoConfig.isCrossPartitionSubscription()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(isA(RequestDetails.class), isA(Subscription.class), eq("Subscription"))).thenReturn(RequestPartitionId.fromPartitionId(1));

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint("http://foo");

		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));

		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRestOperationType(RestOperationTypeEnum.CREATE);

		try {
			mySvc.validateSubmittedSubscription(subscription, requestDetails);
			fail();
		} catch (UnprocessableEntityException theUnprocessableEntityException) {
			assertEquals(Msg.code(2010) + "Cross partition subscription must be created on the default partition", theUnprocessableEntityException.getMessage());
		}
	}

	@Test
	public void testValidate_Cross_Partition_Subscription_Without_Setting() {
		when(myDaoConfig.isCrossPartitionSubscription()).thenReturn(false);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint("http://foo");

		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));

		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRestOperationType(RestOperationTypeEnum.CREATE);

		try {
			mySvc.validateSubmittedSubscription(subscription, requestDetails);
			fail();
		} catch (UnprocessableEntityException theUnprocessableEntityException) {
			assertEquals(Msg.code(2009) + "Cross partition subscription is not enabled on this server", theUnprocessableEntityException.getMessage());
		}
	}

	@Test
	public void testValidate_Cross_Partition_System_Subscription_Without_Setting() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Subscription subscription = new Subscription();
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria("Patient?identifier=foo");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint("http://foo");

		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRestOperationType(RestOperationTypeEnum.CREATE);

		// No asserts here because the function should throw an UnprocessableEntityException exception if the subscription
		// is invalid
		mySvc.validateSubmittedSubscription(subscription, requestDetails);
		Mockito.verify(myDaoConfig, never()).isCrossPartitionSubscription();
		Mockito.verify(myDaoRegistry, times(1)).isResourceTypeSupported(eq("Patient"));
		Mockito.verify(myRequestPartitionHelperSvc, never()).determineCreatePartitionForRequest(isA(RequestDetails.class), isA(Patient.class), eq("Patient"));
	}

}
