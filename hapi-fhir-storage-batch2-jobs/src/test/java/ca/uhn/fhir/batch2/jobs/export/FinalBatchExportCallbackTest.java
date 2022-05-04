package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinalBatchExportCallbackTest {

	@Mock
	private IBulkExportProcessor myProcessor;

	@InjectMocks
	private FinalBatchExportCallback myCallback;

	@Test
	public void jobComplete_succesfully_finalizesTheJobStatus() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobId);
		JobCompletionDetails<BulkExportJobParameters> input = new JobCompletionDetails<>(parameters, "1");

		// when
		when(myProcessor.getJobStatus(eq(jobId)))
			.thenReturn(BulkExportJobStatusEnum.SUBMITTED);

		// test
		myCallback.jobComplete(input);

		// verify
		verify(myProcessor)
			.setJobStatus(eq(jobId), eq(BulkExportJobStatusEnum.COMPLETE), any());
	}

	@Test
	public void jobComplete_jobInErrorState_doesNotSetToComplete() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobId);
		JobCompletionDetails<BulkExportJobParameters> input = new JobCompletionDetails<>(parameters, "1");

		// when
		when(myProcessor.getJobStatus(eq(jobId)))
			.thenReturn(BulkExportJobStatusEnum.ERROR);

		// test
		myCallback.jobComplete(input);

		// verify
		verify(myProcessor, never())
			.setJobStatus(anyString(), any(BulkExportJobStatusEnum.class), any());
	}
}
