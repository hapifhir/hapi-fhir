package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_CLEANUP_DROP_STAGING_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

// Created by Claude Fable 5.2
@ExtendWith(MockitoExtension.class)
class CleanupStep2DropStagingVersionTest {

	private static final String THE_VS_URL = "http://foo";
	private static final String THE_STAGING_VERSION_ID = "the-staging-version-id";

	@Mock
	private ITermValueSetStorageSvc myValueSetStorageSvc;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private JobDefinition<PreExpandValueSetCleanupParameters> myJobDefinition;
	@Mock
	private IJobDataSink<VoidModel> myDataSink;

	private CleanupStep2DropStagingVersion mySvc;

	@BeforeEach
	void before() {
		mySvc = new CleanupStep2DropStagingVersion(myValueSetStorageSvc);
	}

	@ParameterizedTest
	@CsvSource({
		"true,  1",
		// a staging version that is already gone counts as dropped
		"false, 0"
	})
	void run_dropSucceeds_reportsWhetherAnythingWasDropped(boolean theDropped, int theExpectedRecordsProcessed) {
		// Setup
		when(myValueSetStorageSvc.dropStagingVersionIfExists(THE_VS_URL, THE_STAGING_VERSION_ID)).thenReturn(theDropped);

		// Test
		RunOutcome outcome = mySvc.run(newStepExecutionDetails(0), myDataSink);

		// Verify
		assertThat(outcome.getRecordsProcessed()).isEqualTo(theExpectedRecordsProcessed);
	}

	/**
	 * The delay doubles with each retry. A work chunk that is still writing to the staging version finishes
	 * within seconds, so the first retries follow quickly, and the later ones leave time for a longer problem.
	 */
	@ParameterizedTest
	@CsvSource({
		"0, 5",
		"1, 10",
		"3, 40",
		"6, 320",
		// capped at ten minutes
		"7, 600",
		"9, 600"
	})
	void run_dropFails_asksToBeRunAgainAfterADelayThatDoublesWithEachRetry(int thePreviousRetries, int theExpectedDelaySeconds) {
		// Setup
		when(myValueSetStorageSvc.dropStagingVersionIfExists(THE_VS_URL, THE_STAGING_VERSION_ID))
			.thenThrow(new IllegalStateException("a chunk is still writing"));

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(newStepExecutionDetails(thePreviousRetries), myDataSink))
			.isInstanceOfSatisfying(RetryChunkLaterException.class, e ->
				assertThat(e.getNextPollDuration()).isEqualTo(Duration.ofSeconds(theExpectedDelaySeconds)))
			.hasMessage(Msg.code(3078));
	}

	@Test
	void run_dropStillFailsAfterTheLastRetry_letsTheFailureThrough() {
		// Setup
		IllegalStateException failure = new IllegalStateException("a chunk is still writing");
		when(myValueSetStorageSvc.dropStagingVersionIfExists(THE_VS_URL, THE_STAGING_VERSION_ID)).thenThrow(failure);

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(newStepExecutionDetails(CleanupStep2DropStagingVersion.MAX_RETRIES), myDataSink))
			.isSameAs(failure);
	}

	@Test
	void run_notAStagingVersion_letsTheFailureThroughWithoutRetryingLater() {
		// Setup
		InvalidRequestException failure = new InvalidRequestException(Msg.code(2989) + "not a staging version");
		when(myValueSetStorageSvc.dropStagingVersionIfExists(THE_VS_URL, THE_STAGING_VERSION_ID)).thenThrow(failure);

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(newStepExecutionDetails(0), myDataSink))
			.isSameAs(failure);
	}

	private StepExecutionDetails<PreExpandValueSetCleanupParameters, DropStagingVersionWorkChunkJson> newStepExecutionDetails(int thePreviousRetries) {
		PreExpandValueSetCleanupParameters params = new PreExpandValueSetCleanupParameters();
		params.setUrl(THE_VS_URL);

		DropStagingVersionWorkChunkJson data = new DropStagingVersionWorkChunkJson();
		data.setStagingVersionId(THE_STAGING_VERSION_ID);

		WorkChunk workChunk = new WorkChunk();
		workChunk.setPollAttempts(thePreviousRetries);

		return new StepExecutionDetails<>(params, data, new JobInstance(), workChunk, myJobStepExecutionServices, myJobDefinition, STEP_ID_CLEANUP_DROP_STAGING_VERSION, null);
	}
}
