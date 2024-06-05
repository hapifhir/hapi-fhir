package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.r4.BasePartitioningR4Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.resthook.RestHookTestR4Test;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.servlet.ServletException;
import org.awaitility.core.ConditionTimeoutException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartitionedSubscriptionTriggeringR4Test extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR4Test.class);

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@Autowired
	private ISubscriptionTriggeringSvc mySubscriptionTriggeringSvc;

	static final String PARTITION_1 = "PART-1";
	public static final RequestPartitionId REQ_PART_1 = RequestPartitionId.fromPartitionNames(PARTITION_1);
	static final String PARTITION_2 = "PART-2";
	public static final RequestPartitionId REQ_PART_2 = RequestPartitionId.fromPartitionNames(PARTITION_2);

	protected MyReadWriteInterceptor myPartitionInterceptor;
	protected LocalDate myPartitionDate;
	protected LocalDate myPartitionDate2;
	protected int myPartitionId;
	protected int myPartitionId2;


	@BeforeEach
	public void beforeEach() throws ServletException {
		mySubscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());

		myStorageSettings.setUniqueIndexesEnabled(true);

		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionDate2 = LocalDate.of(2020, Month.JANUARY, 15);
		myPartitionId = 1;
		myPartitionId2 = 2;

		myPartitionInterceptor = new MyReadWriteInterceptor();
		myPartitionInterceptor.setRequestPartitionId(REQ_PART_1);

		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
	}

	@AfterEach
	@Override
	public void afterUnregisterRestHookListener() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(new SubscriptionSettings().isTriggerSubscriptionsForNonVersioningChanges());

		mySubscriptionSettings.setCrossPartitionSubscriptionEnabled(false);
		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setUnnamedPartitionMode(false);

		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myDaoRegistry.getSystemDao().expunge(new ExpungeOptions().setExpungeEverything(true), null);
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());

		mySrdInterceptorService.unregisterInterceptorsIf(t -> t instanceof BasePartitioningR4Test.MyReadWriteInterceptor);
		await().until(() -> {
			mySubscriptionTriggeringSvc.runDeliveryPass();
			return ((SubscriptionTriggeringSvcImpl) mySubscriptionTriggeringSvc).getActiveJobCount() == 0;
		});

		super.afterUnregisterRestHookListener();
	}

	@Test
	public void testCreateSubscriptionInPartition() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = newSubscription(criteria1, payload);

		assertEquals(mySrdInterceptorService.getAllRegisteredInterceptors().size(), 1);

		myDaoRegistry.getResourceDao("Subscription").create(subscription, mySrd);

		waitForActivatedSubscriptionCount(1);

		Observation observation = createBaseObservation(code, "SNOMED-CT");
		myDaoRegistry.getResourceDao("Observation").create(observation, mySrd);

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, BaseSubscriptionsR4Test.ourObservationProvider.getCountCreate());
		BaseSubscriptionsR4Test.ourObservationProvider.waitForUpdateCount(1);

		assertEquals(Constants.CT_FHIR_JSON_NEW, BaseSubscriptionsR4Test.ourRestfulServer.getRequestContentTypes().get(0));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCreateSubscriptionInPartitionAndResourceInDifferentPartition(boolean theIsCrossPartitionEnabled) throws Exception {
		mySubscriptionSettings.setCrossPartitionSubscriptionEnabled(theIsCrossPartitionEnabled);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Patient?active=true";
		Subscription subscription = newSubscription(criteria1, payload);

		assertEquals(mySrdInterceptorService.getAllRegisteredInterceptors().size(), 1);

		myDaoRegistry.getResourceDao("Subscription").create(subscription, mySrd);

		waitForActivatedSubscriptionCount(1);

		Patient patient = new Patient();
		patient.setActive(true);
		myDaoRegistry.getResourceDao("Patient").create(patient, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));

		// Should see 0 subscription notification
		waitForQueueToDrain();
		assertEquals(0, BaseSubscriptionsR4Test.ourPatientProvider.getCountCreate());

		try {
			BaseSubscriptionsR4Test.ourPatientProvider.waitForUpdateCount(1);
			if (!theIsCrossPartitionEnabled) {
				fail("Expecting a timeout and 0 matching subscriptions and thus a timeout if cross partition is DISabled");
			}
			assertEquals(1, BaseSubscriptionsR4Test.ourRestfulServer.getRequestContentTypes().size());
		} catch (ConditionTimeoutException e) {
			if (theIsCrossPartitionEnabled) {
				fail("Expecting no timeout and 1 matching subscriptions and thus a timeout if cross partition is enabled");
			} else {
				// Should have 0 matching subscription, if we get 1 update count then the test fails
				assertEquals(0, BaseSubscriptionsR4Test.ourRestfulServer.getRequestContentTypes().size());
			}
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testManualTriggeredSubscriptionDoesNotCheckOutsideOfPartition(boolean theIsCrossPartitionEnabled) throws Exception {
		mySubscriptionSettings.setCrossPartitionSubscriptionEnabled(theIsCrossPartitionEnabled);
		String payload = "application/fhir+json";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		//Given: We store a resource in partition 2
		myPartitionInterceptor.setRequestPartitionId(REQ_PART_2);
		final IFhirResourceDao observation = myDaoRegistry.getResourceDao("Observation");
		IIdType observationIdPartitionTwo = observation.create(createBaseObservation(code, "SNOMED-CT"), mySrd).getId();

		//Given: We store a similar resource in partition 1
		myPartitionInterceptor.setRequestPartitionId(REQ_PART_1);
		IIdType observationIdPartitionOne = observation.create(createBaseObservation(code, "SNOMED-CT"), mySrd).getId();

		//Given: We create a subscrioption on Partition 1
		IIdType subscriptionId = myDaoRegistry.getResourceDao("Subscription").create(newSubscription(criteria1, payload), mySrd).getId();
		waitForActivatedSubscriptionCount(1);

		ArrayList<IPrimitiveType<String>> searchUrlList = new ArrayList<>();
		searchUrlList.add(new StringDt("Observation?"));

		Parameters resultParameters = (Parameters) mySubscriptionTriggeringSvc.triggerSubscription(null, searchUrlList, subscriptionId, mySrd);
		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForQueueToDrain();
		List<Observation> resourceUpdates = BaseSubscriptionsR4Test.ourObservationProvider.getResourceUpdates();
		if (theIsCrossPartitionEnabled) {
			assertEquals(2, resourceUpdates.size());
			assertEquals(Stream.of(observationIdPartitionOne, observationIdPartitionTwo).map(Object::toString).sorted().toList(),
				resourceUpdates.stream().map(Resource::getId).sorted().toList());
		} else {
			assertEquals(1, resourceUpdates.size());
			assertEquals(observationIdPartitionOne.toString(), resourceUpdates.get(0).getId());
		}

		String responseValue = resultParameters.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue).contains("Subscription triggering job submitted as JOB ID");
	}

	@Test
	public void testManualTriggeredSubscriptionWithCrossPartitionChecksBothPartitions() throws Exception {
		String payload = "application/fhir+json";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		//Given: We store a resource in partition 2
		myPartitionInterceptor.setRequestPartitionId(REQ_PART_2);
		myDaoRegistry.getResourceDao("Observation").create(createBaseObservation(code, "SNOMED-CT"), mySrd).getId();

		//Given: We store a similar resource in partition 1
		myPartitionInterceptor.setRequestPartitionId(REQ_PART_1);
		myDaoRegistry.getResourceDao("Observation").create(createBaseObservation(code, "SNOMED-CT"), mySrd).getId();

		//Given: We create a subscription on Partition 1
		Subscription theResource = newSubscription(criteria1, payload);
		theResource.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType(Boolean.TRUE));
		myPartitionInterceptor.setRequestPartitionId(RequestPartitionId.defaultPartition());
		IIdType subscriptionId = myDaoRegistry.getResourceDao("Subscription").create(theResource, mySrd).getId();
		waitForActivatedSubscriptionCount(1);

		ArrayList<IPrimitiveType<String>> searchUrlList = new ArrayList<>();
		searchUrlList.add(new StringDt("Observation?"));

		myPartitionInterceptor.setRequestPartitionId(RequestPartitionId.defaultPartition());
		mySubscriptionTriggeringSvc.triggerSubscription(null, searchUrlList, subscriptionId, mySrd);
		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForQueueToDrain();
		List<Observation> resourceUpdates = BaseSubscriptionsR4Test.ourObservationProvider.getResourceUpdates();
		assertThat(resourceUpdates).hasSize(2);
	}

	@Test
	public void testManualTriggeredSubscriptionInPartition() throws Exception {
		String payload = "application/fhir+json";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		// Create the resource first
		DaoMethodOutcome observationOutcome = myDaoRegistry.getResourceDao("Observation").create(createBaseObservation(code, "SNOMED-CT"), mySrd);

		Observation observation = (Observation) observationOutcome.getResource();

		// Create the subscription now
		DaoMethodOutcome subscriptionOutcome = myDaoRegistry.getResourceDao("Subscription").create(newSubscription(criteria1, payload), mySrd);

		assertEquals(mySrdInterceptorService.getAllRegisteredInterceptors().size(), 1);

		Subscription subscription = (Subscription) subscriptionOutcome.getResource();

		waitForActivatedSubscriptionCount(1);

		ArrayList<IPrimitiveType<String>> resourceIdList = new ArrayList<>();
		resourceIdList.add(observation.getIdElement());


		Parameters resultParameters = (Parameters) mySubscriptionTriggeringSvc.triggerSubscription(resourceIdList, null, subscription.getIdElement(), mySrd);
		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForQueueToDrain();
		assertEquals(1, BaseSubscriptionsR4Test.ourObservationProvider.getCountUpdate());

		String responseValue = resultParameters.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue).contains("Subscription triggering job submitted as JOB ID");
	}

	@Interceptor
	public static class MyReadWriteInterceptor {
		private RequestPartitionId myReadPartitionId;

		public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
			myReadPartitionId = theRequestPartitionId;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId read(ServletRequestDetails theSrd) {
			RequestPartitionId retVal = myReadPartitionId;
			ourLog.info("Returning partition for read: {}", retVal);
			return retVal;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId create(ServletRequestDetails theSrd) {
			RequestPartitionId retVal = myReadPartitionId;
			ourLog.info("Returning partition for write: {}", retVal);
			return retVal;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId any() {
			return myReadPartitionId;
		}
	}
}
