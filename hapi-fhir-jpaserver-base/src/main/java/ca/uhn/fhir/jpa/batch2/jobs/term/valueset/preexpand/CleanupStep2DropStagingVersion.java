/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

/**
 * Last step of the staging version cleanup job: drops one staging version.
 * <p>
 * A drop can fail while a work chunk of the pre-expansion job that staged the version is still writing to
 * it. batch2 does not start the queued chunks of an ended job, so only the chunks that were executing when
 * the job ended can do that, and they finish soon. The drop is therefore tried again later instead of
 * being left to the error retries of batch2, which follow each other too closely to outlast such a chunk.
 * The delay doubles with each retry, so that the first retries follow within seconds and the later ones
 * leave time for a problem that lasts longer.
 * </p>
 */
// Created by Claude Fable 5.2
public class CleanupStep2DropStagingVersion
		implements IJobStepWorker<PreExpandValueSetCleanupParameters, DropStagingVersionWorkChunkJson, VoidModel> {
	private static final Logger ourLog = LoggerFactory.getLogger(CleanupStep2DropStagingVersion.class);

	/**
	 * How many times a failed drop is tried again later before the failure is let through.
	 */
	static final int MAX_RETRIES = 10;

	/**
	 * The delay before the first retry of a failed drop. batch2 wakes a waiting work chunk on its maintenance
	 * pass, which runs every five seconds by default, so a shorter delay would gain nothing.
	 */
	private static final Duration INITIAL_RETRY_DELAY = Duration.of(5, ChronoUnit.SECONDS);

	/**
	 * The longest delay between two retries of a failed drop.
	 */
	private static final Duration MAX_RETRY_DELAY = Duration.of(10, ChronoUnit.MINUTES);

	private final ITermValueSetStorageSvc myTermValueSetStorageSvc;

	public CleanupStep2DropStagingVersion(@Nonnull ITermValueSetStorageSvc theTermValueSetStorageSvc) {
		myTermValueSetStorageSvc = theTermValueSetStorageSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PreExpandValueSetCleanupParameters, DropStagingVersionWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {
		String url = theStepExecutionDetails.getParameters().getUrl();
		String stagingVersionId = theStepExecutionDetails.getData().getStagingVersionId();

		boolean dropped;
		try {
			dropped = myTermValueSetStorageSvc.dropStagingVersionIfExists(url, stagingVersionId);
		} catch (InvalidRequestException e) {
			// Not a staging version: trying again later would not change that
			throw e;
		} catch (RuntimeException e) {
			int retries = getIfNull(theStepExecutionDetails.getWorkChunk().getPollAttempts(), 0);
			if (retries >= MAX_RETRIES) {
				throw e;
			}
			Duration retryDelay = getRetryDelay(retries);
			ourLog.atWarn()
					.setMessage(
							"Failed to drop staging version {} of ValueSet[url={}], retry {} of {} will follow in {}")
					.addArgument(stagingVersionId)
					.addArgument(url)
					.addArgument(retries + 1)
					.addArgument(MAX_RETRIES)
					.addArgument(retryDelay)
					.setCause(e)
					.log();
			throw new RetryChunkLaterException(Msg.code(3078), retryDelay);
		}

		ourLog.atInfo()
				.setMessage("Staging version {} of ValueSet[url={}] {}")
				.addArgument(stagingVersionId)
				.addArgument(url)
				.addArgument(dropped ? "was dropped" : "was already gone")
				.log();

		return new RunOutcome(dropped ? 1 : 0);
	}

	/**
	 * Returns the delay before the next retry: {@link #INITIAL_RETRY_DELAY} doubled once for each retry
	 * already made, and never more than {@link #MAX_RETRY_DELAY}.
	 */
	private static Duration getRetryDelay(int thePreviousRetries) {
		Duration retryDelay = INITIAL_RETRY_DELAY.multipliedBy(1L << Math.min(thePreviousRetries, MAX_RETRIES));
		return retryDelay.compareTo(MAX_RETRY_DELAY) > 0 ? MAX_RETRY_DELAY : retryDelay;
	}
}
