package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.config.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.dao.r4.BasePartitioningR4Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.resthook.RestHookTestR4Test;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartitionedSubscriptionTriggeringR4Test extends BaseSubscriptionsR4Test  {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR4Test.class);

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	static final String PARTITION_1 = "PART-1";

	protected MyReadWriteInterceptor myPartitionInterceptor;
	protected LocalDate myPartitionDate;
	protected LocalDate myPartitionDate2;
	protected int myPartitionId;
	protected int myPartitionId2;


	@BeforeEach
	public void beforeEach() throws ServletException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());

		myDaoConfig.setUniqueIndexesEnabled(true);

		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionDate2 = LocalDate.of(2020, Month.JANUARY, 15);
		myPartitionId = 1;
		myPartitionId2 = 2;

		myPartitionInterceptor = new MyReadWriteInterceptor();
		myPartitionInterceptor.setResultPartitionId(RequestPartitionId.fromPartitionNames(PARTITION_1));

		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}

	@AfterEach
	@Override
	public void afterUnregisterRestHookListener() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		myDaoConfig.setTriggerSubscriptionsForNonVersioningChanges(new DaoConfig().isTriggerSubscriptionsForNonVersioningChanges());

		myDaoRegistry.getSystemDao().expunge(new ExpungeOptions().setExpungeEverything(true), null);

		myPartitionSettings.setUnnamedPartitionMode(false);

		mySrdInterceptorService.unregisterInterceptorsIf(t -> t instanceof BasePartitioningR4Test.MyReadWriteInterceptor);
		myInterceptor = null;
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

		assertEquals(Constants.CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
	}

	@Interceptor
	public static class MyReadWriteInterceptor {
		private RequestPartitionId myReadPartitionId;

		public void setResultPartitionId(RequestPartitionId theRequestPartitionId) {
			myReadPartitionId = theRequestPartitionId;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId read() {
			RequestPartitionId retVal = myReadPartitionId;
			ourLog.info("Returning partition for read: {}", retVal);
			return retVal;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId create() {
			RequestPartitionId retVal = myReadPartitionId;
			ourLog.info("Returning partition for write: {}", retVal);
			return retVal;
		}
	}
}
