package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BaseBulkModifyOrRewriteGenerateReportStepTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseBulkModifyOrRewriteGenerateReportStepTest.class);
	@Mock
	private IJobDataSink<BulkModifyResourcesResultsJson> mySink;
	@Captor
	private ArgumentCaptor<BulkModifyResourcesResultsJson> myDataCaptor;

	@Test
	void testReportTruncatesFailures() {
		MySvc svc = new MySvc();

		BulkModifyResourcesChunkOutcomeJson data = new BulkModifyResourcesChunkOutcomeJson();
		for (int i = 0; i < 200; i++) {
			data.addFailure(new IdType("Patient/" + i), "Failure Message " + i);
		}
		MyParams params = new MyParams();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("an-instance-id");
		instance.setStartTime(new Date());
		instance.setEndTime(new Date());
		instance.setStatus(StatusEnum.COMPLETED);
		String instanceId = instance.getInstanceId();
		String chunkId = null;
		ChunkExecutionDetails<MyParams, BulkModifyResourcesChunkOutcomeJson> chunk = new ChunkExecutionDetails<>(data, params, instanceId, chunkId);

		svc.consume(chunk);

		// Test
		StepExecutionDetails<MyParams, BulkModifyResourcesChunkOutcomeJson> executionDetails = new StepExecutionDetails<>(params, null, instance, new WorkChunk());
		ReductionStepFailureException exception = assertThrows(ReductionStepFailureException.class, () -> svc.run(executionDetails, mySink));

		// Verify
		String report = ((BulkModifyResourcesResultsJson)exception.getReportMsg()).getReport();
		ourLog.info(report);
		assertThat(report).containsSubsequence(
			"Failures:",
			"Patient/10: Failure Message 10",
			"...truncated 100 failures..."
		);
	}

	private static class MyParams extends BaseBulkModifyJobParameters {
	}

	private class MySvc extends BaseBulkModifyOrRewriteGenerateReportStep<MyParams> {

		@Nonnull
		@Override
		protected String provideJobName() {
			return "Sample Job";
		}

		@Override
		public MySvc newInstance() {
			return this;
		}
	}
}
