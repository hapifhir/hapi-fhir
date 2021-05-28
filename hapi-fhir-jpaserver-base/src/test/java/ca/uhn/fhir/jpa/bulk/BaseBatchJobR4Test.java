package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseBatchJobR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseBatchJobR4Test.class);
	@Autowired
	private JobExplorer myJobExplorer;

	protected List<JobExecution> awaitAllBulkJobCompletions(String... theJobNames) {
		assert theJobNames.length > 0;

		List<JobInstance> bulkExport = new ArrayList<>();
		for (String nextName : theJobNames) {
			bulkExport.addAll(myJobExplorer.findJobInstancesByJobName(nextName, 0, 100));
		}
		if (bulkExport.isEmpty()) {
			List<String> wantNames = Arrays.asList(theJobNames);
			List<String> haveNames = myJobExplorer.getJobNames();
			fail("There are no jobs running - Want names " + wantNames + " and have names " + haveNames);
		}
		List<JobExecution> bulkExportExecutions = bulkExport.stream().flatMap(jobInstance -> myJobExplorer.getJobExecutions(jobInstance).stream()).collect(Collectors.toList());
		awaitJobCompletions(bulkExportExecutions);

		// Return the final state
		bulkExportExecutions = bulkExport.stream().flatMap(jobInstance -> myJobExplorer.getJobExecutions(jobInstance).stream()).collect(Collectors.toList());
		return bulkExportExecutions;
	}

	protected void awaitJobCompletions(Collection<JobExecution> theJobs) {
		theJobs.forEach(jobExecution -> awaitJobCompletion(jobExecution));
	}

	protected void awaitJobCompletion(JobExecution theJobExecution) {
		await().atMost(120, TimeUnit.SECONDS).until(() -> {
			JobExecution jobExecution = myJobExplorer.getJobExecution(theJobExecution.getId());
			ourLog.info("JobExecution {} currently has status: {}- Failures if any: {}", theJobExecution.getId(), jobExecution.getStatus(), jobExecution.getFailureExceptions());
			return jobExecution.getStatus() == BatchStatus.COMPLETED || jobExecution.getStatus() == BatchStatus.FAILED;
		});
	}

}
