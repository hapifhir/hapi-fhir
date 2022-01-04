package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;

public class BatchJobHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchJobHelper.class);
	private final JobExplorer myJobExplorer;

	public BatchJobHelper(JobExplorer theJobExplorer) {
		myJobExplorer = theJobExplorer;
	}


	public List<JobExecution> awaitAllBulkJobCompletions(String... theJobNames) {
		return awaitAllBulkJobCompletions(true, theJobNames);
	}

	/**
	 * Await and report for job completions
	 * @param theFailIfNotJobsFound indicate if must fail in case no matching jobs are found
	 * @param theJobNames The job names to match
	 * @return the matched JobExecution(s)
	 */
	public List<JobExecution> awaitAllBulkJobCompletions(boolean theFailIfNotJobsFound, String... theJobNames) {
		assert theJobNames.length > 0;

		if (theFailIfNotJobsFound) {
			await().until(() -> !getJobInstances(theJobNames).isEmpty());
		}
		List<JobInstance> matchingJobInstances = getJobInstances(theJobNames);

		if (theFailIfNotJobsFound) {
			if (matchingJobInstances.isEmpty()) {
				List<String> wantNames = Arrays.asList(theJobNames);
				List<String> haveNames = myJobExplorer.getJobNames();
				fail("There are no jobs running - Want names " + wantNames + " and have names " + haveNames);
			}
		}
		List<JobExecution> matchingExecutions = matchingJobInstances.stream().flatMap(jobInstance -> myJobExplorer.getJobExecutions(jobInstance).stream()).collect(Collectors.toList());
		awaitJobCompletions(matchingExecutions);

		// Return the final state
		matchingExecutions = matchingJobInstances.stream().flatMap(jobInstance -> myJobExplorer.getJobExecutions(jobInstance).stream()).collect(Collectors.toList());
		return matchingExecutions;
	}

	private List<JobInstance> getJobInstances(String[] theJobNames) {
		List<JobInstance> matchingJobInstances = new ArrayList<>();
		for (String nextName : theJobNames) {
			matchingJobInstances.addAll(myJobExplorer.findJobInstancesByJobName(nextName, 0, 100));
		}
		return matchingJobInstances;
	}

	public JobExecution awaitJobExecution(Long theJobExecutionId) {
		JobExecution jobExecution = myJobExplorer.getJobExecution(theJobExecutionId);
		awaitJobCompletion(jobExecution);
		return myJobExplorer.getJobExecution(theJobExecutionId);
	}

	protected void awaitJobCompletions(Collection<JobExecution> theJobs) {
		theJobs.forEach(jobExecution -> awaitJobCompletion(jobExecution));
	}

	public void awaitJobCompletion(JobExecution theJobExecution) {
		await().atMost(120, TimeUnit.SECONDS).until(() -> {
			JobExecution jobExecution = myJobExplorer.getJobExecution(theJobExecution.getId());
			ourLog.info("JobExecution {} currently has status: {}- Failures if any: {}", theJobExecution.getId(), jobExecution.getStatus(), jobExecution.getFailureExceptions());
			// JM: Adding ABANDONED status because given the description, it s similar to FAILURE, and we need to avoid tests failing because
			// of wait timeouts caused by unmatched statuses. Also adding STOPPED because tests were found where this wait timed out
			// with jobs keeping that status during the whole wait
			return jobExecution.getStatus() == BatchStatus.COMPLETED || jobExecution.getStatus() == BatchStatus.FAILED
				|| jobExecution.getStatus() == BatchStatus.ABANDONED || jobExecution.getStatus() == BatchStatus.STOPPED;
		});
	}

	public int getReadCount(Long theJobExecutionId) {
		StepExecution stepExecution = getStepExecution(theJobExecutionId);
		return stepExecution.getReadCount();
	}

	public int getWriteCount(Long theJobExecutionId) {
		StepExecution stepExecution = getStepExecution(theJobExecutionId);
		return stepExecution.getWriteCount();
	}

	private StepExecution getStepExecution(Long theJobExecutionId) {
		JobExecution jobExecution = myJobExplorer.getJobExecution(theJobExecutionId);
		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
		assertThat(stepExecutions, hasSize(1));
		return stepExecutions.iterator().next();
	}

}
