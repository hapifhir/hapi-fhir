package ca.uhn.fhir.batch2.api;

import org.springframework.transaction.annotation.Transactional;

public interface IJobStateEvents {

	@Transactional
	void processCancelRequests();

	@Transactional
	void updateRunningJobStatistics();
}
