package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.batch2.api.IJobCleanerService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class Batch2JobHelper {

	@Autowired
	private IJobCleanerService myJobCleanerService;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	public void awaitJobCompletion(String theId) {
		await().until(() -> {
			myJobCleanerService.runCleanupPass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, equalTo(StatusEnum.COMPLETED));
	}

}
