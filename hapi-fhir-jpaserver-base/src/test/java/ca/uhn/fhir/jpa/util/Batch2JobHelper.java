package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.batch2.api.IJobCleanerService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;

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

	public JobInstance awaitJobFailure(String theId) {
		await().until(() -> {
			myJobCleanerService.runCleanupPass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, Matchers.anyOf(equalTo(StatusEnum.ERRORED),equalTo(StatusEnum.FAILED)));
		return myJobCoordinator.getInstance(theId);
	}

}
