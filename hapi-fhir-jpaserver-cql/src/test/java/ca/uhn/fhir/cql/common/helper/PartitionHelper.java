package ca.uhn.fhir.cql.common.helper;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PartitionHelper implements BeforeEachCallback, AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionHelper.class);
	protected final MyTestInterceptor myInterceptor = new MyTestInterceptor();

	@Autowired
	IInterceptorService myIInterceptorService;
	@Autowired
	PartitionSettings myPartitionSettings;

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myPartitionSettings.setPartitioningEnabled(true);
		myIInterceptorService.registerInterceptor(myInterceptor);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myIInterceptorService.unregisterInterceptor(myInterceptor);
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptor.clear();
	}

	public void clear() {
		myInterceptor.clear();
	}

	public boolean wasCalled() {
		return myInterceptor.wasCalled();
	}

	public static class MyTestInterceptor {
		private boolean myCalled = false;

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		RequestPartitionId partitionIdentifyRead(RequestDetails theRequestDetails) {
			myCalled = true;
			if (theRequestDetails == null) {
				ourLog.info("useful breakpoint :-)");
			}
			assertNotNull(theRequestDetails);
			return RequestPartitionId.defaultPartition();
		}

		public void clear() {
			myCalled = false;
		}

		public boolean wasCalled() {
			return myCalled;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		RequestPartitionId partitionIdentifyCreate(RequestDetails theRequestDetails) {
			return RequestPartitionId.defaultPartition();
		}
	}
}
