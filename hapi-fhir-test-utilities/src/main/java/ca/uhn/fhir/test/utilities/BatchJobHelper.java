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

import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
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
import static org.hamcrest.Matchers.*;
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
	 *
	 * @param theFailIfNotJobsFound indicate if must fail in case no matching jobs are found
	 * @param theJobNames           The job names to match
	 * @return the matched JobExecution(s)
	 */
	public List<JobExecution> awaitAllBulkJobCompletions(boolean theFailIfNotJobsFound, String... theJobNames) {
		assert theJobNames.length > 0;

		if (theFailIfNotJobsFound) {
			await()
				.alias("Wait for jobs to exist named: " + Arrays.asList(theJobNames))
				.until(() -> getJobInstances(theJobNames), not(empty()));
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
		// This intermittently fails for unknown reasons, so I've added a bunch
		// of extra junk here to improve what we output when it fails
		for (JobExecution jobExecution : theJobs) {
			try {
				awaitJobCompletion(jobExecution);
			} catch (ConditionTimeoutException e) {
				StringBuilder msg = new StringBuilder();
				msg.append("Failed waiting for job to complete.\n");
				msg.append("Error: ").append(e).append("\n");
				msg.append("Statuses:");
				for (JobExecution next : theJobs) {
					JobExecution execution = myJobExplorer.getJobExecution(next.getId());
					msg.append("\n * Execution ")
						.append(execution.getId())
						.append(" has status ")
						.append(execution.getStatus());
				}
				fail(msg.toString());
			}
		}
	}

	public void awaitJobCompletion(JobExecution theJobExecution) {
		await()
			.atMost(120, TimeUnit.SECONDS).until(() -> {
				JobExecution jobExecution = myJobExplorer.getJobExecution(theJobExecution.getId());
				ourLog.info("JobExecution {} currently has status: {}- Failures if any: {}", theJobExecution.getId(), jobExecution.getStatus(), jobExecution.getFailureExceptions());
				return jobExecution.getStatus();
			}, Matchers.oneOf(
				// JM: Adding ABANDONED status because given the description, it s similar to FAILURE, and we need to avoid tests failing because
				// of wait timeouts caused by unmatched statuses. Also adding STOPPED because tests were found where this wait timed out
				// with jobs keeping that status during the whole wait
				// KHS: Adding UNKNOWN
				BatchStatus.COMPLETED,
				BatchStatus.FAILED,
				BatchStatus.ABANDONED,
				BatchStatus.STOPPED,
				BatchStatus.UNKNOWN
			));
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

	public void ensureNoRunningJobs() {
		for (String nextJobName : myJobExplorer.getJobNames()) {
			List<JobInstance> instances = myJobExplorer.getJobInstances(nextJobName, 0, 10000);
			for (JobInstance nextInstance : instances) {
				List<JobExecution> executions = myJobExplorer.getJobExecutions(nextInstance);
				for (JobExecution nextExecution : executions) {
					ourLog.info("Have job execution {} in status: {}", nextExecution.getId(), nextExecution.getStatus());
					try {
						await().until(() -> myJobExplorer.getJobExecution(nextExecution.getId()).getStatus(), oneOf(BatchStatus.STOPPED, BatchStatus.ABANDONED, BatchStatus.FAILED, BatchStatus.COMPLETED));
					} catch (ConditionTimeoutException e) {
						JobExecution execution = myJobExplorer.getJobExecution(nextExecution.getId());
						fail("Execution " + execution + "\n" +
							"Instance: " + nextInstance + "\n" +
							"Job: " + nextJobName);
					}

				}
			}
		}
	}
}
