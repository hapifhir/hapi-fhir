package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration(classes = {MdmHelperConfig.class, MdmPartitionIT.PartitionConfiguration.class})
public class MdmPartitionIT extends BaseMdmR4Test {
	private static final Logger ourLog = getLogger(MdmPartitionIT.class);

	@Configuration
	public static class PartitionConfiguration {
		@Autowired
		private PartitionSettings myPartitionSettings;

		@Autowired
		private IPartitionLookupSvc myPartitionLookupSvc;

		@Autowired
		private IInterceptorService myInterceptorService;

		// a bean so we can access it here and in the test suite
		@Bean
		MyTestInterceptor testInterceptor() {
			return new MyTestInterceptor();
		}

		@PostConstruct
		public void init() {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionSettings.setDefaultPartitionId(10);
			myPartitionSettings.setUnnamedPartitionMode(false);
			myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
			myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);

			myInterceptorService.registerInterceptor(testInterceptor());
		}
	}

	private static class MyTestInterceptor {

		private RequestPartitionId myPartitionIdToSpoof;

		public void setPartitionIdToSpoof(RequestPartitionId theRequestPartitionId) {
			myPartitionIdToSpoof = theRequestPartitionId;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId hook(RequestDetails theRequestDetails) {
			if (myPartitionIdToSpoof != null) {
				return myPartitionIdToSpoof;
			}
			return RequestPartitionId.fromPartitionId(10);
		}

		@Hook(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS)
		public void didNotFind(ResourceModifiedMessage theMsg) {
			// in case we want to know why we can log
			// but this is fired for all subscriptions on startup too
			ourLog.debug(theMsg.toString());
		}
	}

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private MyTestInterceptor myInterceptor;

	@Override
	public void beforeUnregisterAllSubscriptions() {
		// noop
	}

	@Test
	public void createResource_withNonDefaultIdPartition_shouldCreate() throws InterruptedException {
		// setup
		Patient patient = buildFrankPatient();

		long initialCount = myMdmLinkDao.count();

		// test
		myMdmHelper.createWithLatch(patient, true);

		// verify (1 created)
		assertEquals(initialCount + 1, myMdmLinkDao.count());
	}
}
