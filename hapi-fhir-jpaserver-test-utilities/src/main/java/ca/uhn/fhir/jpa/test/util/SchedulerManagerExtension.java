package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerManagerExtension implements AfterEachCallback, BeforeEachCallback {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerManagerExtension.class);

	public interface ServiceSupplier {

		ISchedulerService getSchedulerService();
	}

	private final ServiceSupplier mySupplier;

	public SchedulerManagerExtension(ServiceSupplier theServiceSupplier) {
		mySupplier = theServiceSupplier;
	}

	@Override
	@Order(value = 0)
	public void afterEach(ExtensionContext context) throws Exception {
		ourLog.info("Temporarily stopping all scheduled jobs");

		mySupplier.getSchedulerService().pauseAllJobs(true);
	}

	@Override
	@Order(value = 0)
	public void beforeEach(ExtensionContext context) throws Exception {
		ourLog.info("Starting or restarting all scheduled jobs");
		mySupplier.getSchedulerService().pauseAllJobs(false);
	}
}
